/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.postgres.client.repositories.*;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.ScopeReference;
import com.cartobucket.auth.postgres.client.entities.mappers.ApplicationMapper;
import com.cartobucket.auth.postgres.client.entities.mappers.ApplicationSecretMapper;
import com.cartobucket.auth.postgres.client.entities.mappers.ProfileMapper;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ApplicationService implements com.cartobucket.auth.data.services.ApplicationService {
    final ApplicationRepository applicationRepository;
    final ApplicationSecretRepository applicationSecretRepository;
    final EventRepository eventRepository;
    final ProfileRepository profileRepository;
    final ScopeReferenceRepository scopeReferenceRepository;
    final ScopeService scopeService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ApplicationSecretRepository applicationSecretRepository,
            EventRepository eventRepository,
            ProfileRepository profileRepository,
            ScopeReferenceRepository scopeReferenceRepository, ScopeService scopeService
    ) {
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
        this.eventRepository = eventRepository;
        this.profileRepository = profileRepository;
        this.scopeReferenceRepository = scopeReferenceRepository;
        this.scopeService = scopeService;
    }

    @Override
    @Transactional
    public void deleteApplication(final UUID applicationId) throws ApplicationNotFound {
        final var application = applicationRepository
                .findByIdOptional(applicationId)
                .orElseThrow(ApplicationNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(
                        applicationId,
                        ProfileType.Application
                )
                .orElseThrow(ProfileNotFound::new);
        applicationRepository.delete(application);
        profileRepository.delete(profile);
        eventRepository.createApplicationProfileEvent(
                Pair.Companion.create(ApplicationMapper.from(application), ProfileMapper.from(profile)),
                EventType.DELETE
        );
    }

    @Override
    @Transactional
    public Pair<Application, Profile> getApplication(final UUID applicationId)
            throws ApplicationNotFound, ProfileNotFound {
        final var application = applicationRepository
                .findByIdOptional(applicationId)
                .map(ApplicationMapper::from)
                .orElseThrow(ApplicationNotFound::new);
        final var profile = profileRepository
                .findByResourceAndProfileType(
                        applicationId,
                        ProfileType.Application
                )
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        return Pair.Companion.create(application, profile);
    }

    @Override
    public Pair<Application, Profile> createApplication(final Application application, final Profile profile) {
        QuarkusTransaction.begin();
        application.setCreatedOn(OffsetDateTime.now());
        application.setUpdatedOn(OffsetDateTime.now());
        var _application = ApplicationMapper.to(application);
        applicationRepository.persist(_application);

        profile.setResource(_application.getId());
        profile.setProfileType(ProfileType.Application);
        profile.setAuthorizationServerId(_application.getAuthorizationServerId());
        profile.setCreatedOn(OffsetDateTime.now());
        profile.setUpdatedOn(OffsetDateTime.now());
        var _profile = ProfileMapper.to(profile);
        profileRepository.persist(_profile);

        for (var scope : application.getScopes()) {
            final var scopeReference = new ScopeReference();
            scopeReference.setScopeId(scope.getId());
            scopeReference.setResourceId(_application.getId());
            scopeReference.setScopeReferenceType(ScopeReference.ScopeReferenceType.APPLICATION);
            scopeReferenceRepository.persist(scopeReference);
        }
        QuarkusTransaction.commit();

        QuarkusTransaction.begin();
        var applicationProfilePair = Pair.Companion.create(
                ApplicationMapper.from(applicationRepository.findById(_application.getId())),
                ProfileMapper.from(_profile)
        );
        // TODO: This second transaction is wrong and not really needed. Have to save for the scopes to get added,
        //  but that should not be that important for the even.
        eventRepository.createApplicationProfileEvent(applicationProfilePair, EventType.CREATE);
        QuarkusTransaction.commit();
        return applicationProfilePair;
    }

    @Override
    @Transactional
    public List<ApplicationSecret> getApplicationSecrets(final List<UUID> applicationIds) throws ApplicationNotFound {
        return applicationSecretRepository.findByApplicationIdIn(applicationIds)
                .stream()
                .map(ApplicationSecretMapper::from)
                .toList();
    }

    @Override
    public ApplicationSecret createApplicationSecret(
            final ApplicationSecret applicationSecret) throws ApplicationNotFound {
        QuarkusTransaction.begin();
        final var application = applicationRepository
                .findByIdOptional(applicationSecret.getApplicationId())
                .orElseThrow(ApplicationNotFound::new);

        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            var secret = new BigInteger(1, new SecureRandom().generateSeed(120)).toString(16);
            var secretHash = new BigInteger(
                    1, messageDigest
                    .digest(secret.getBytes()))
                    .toString(16);
            applicationSecret.setApplicationSecret(secret);
            applicationSecret.setApplicationSecretHash(secretHash);
            applicationSecret.setAuthorizationServerId(application.getAuthorizationServerId());
            applicationSecret.setCreatedOn(OffsetDateTime.now());
            var _applicationSecret = ApplicationSecretMapper.to(applicationSecret);
            applicationSecretRepository.persist(_applicationSecret);
            var applicationSecretReferences = applicationSecret
                    .getScopes()
                    .stream()
                    .map(
                            scope -> {
                                final var scopeReference = new ScopeReference();
                                scopeReference.setScopeId(scope.getId());
                                scopeReference.setResourceId(_applicationSecret.getId());
                                scopeReference.setScopeReferenceType(ScopeReference.ScopeReferenceType.APPLICATION_SECRET);
                                return scopeReference;
                            }
                            )
                    .toList();
            scopeReferenceRepository.persist(applicationSecretReferences);
            QuarkusTransaction.commit();

            // Refresh the secret to get the new scopes and add the actual secret string back on.
            final var refreshedSecret = applicationSecretRepository.findById(_applicationSecret.getId());
            refreshedSecret.setApplicationSecret(secret);
            return ApplicationSecretMapper.from(refreshedSecret);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteApplicationSecret(final UUID secretId)
            throws ApplicationSecretNoApplicationBadData,
            ApplicationSecretNotFound
    {
        final var secret = applicationSecretRepository
                .findByIdOptional(secretId)
                .orElseThrow(ApplicationSecretNotFound::new);
        applicationSecretRepository.delete(secret);
    }

    @Override
    @Transactional
    public List<Application> getApplications(final List<UUID> authorizationServerIds, Page page) {
        if (authorizationServerIds.isEmpty()) {
            return applicationRepository
                    .findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .stream()
                    .map(ApplicationMapper::from)
                    .toList();
        } else {
            return applicationRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .stream()
                    .map(ApplicationMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public boolean isApplicationSecretValid(UUID authorizationServerId, UUID applicationId, String applicationSecret) {
        try {
            final var messageDigest = MessageDigest.getInstance("SHA-256");
            final var secretHash = new BigInteger(
                    1,
                    messageDigest.digest(applicationSecret.getBytes())
            ).toString(16);

            final var _applicationSecret = applicationSecretRepository
                    .findByApplicationSecretHash(secretHash);
            if (_applicationSecret.isEmpty()) {
                return false;
            }

            if (!applicationId.equals(_applicationSecret.get().getApplicationId())) {
                throw new ApplicationSecretNoApplicationBadData();
            }

            return true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
