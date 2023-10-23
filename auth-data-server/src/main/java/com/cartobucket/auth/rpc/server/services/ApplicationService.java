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

package com.cartobucket.auth.rpc.server.services;

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
import com.cartobucket.auth.rpc.server.entities.EventType;
import com.cartobucket.auth.rpc.server.entities.mappers.ApplicationMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ApplicationSecretMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.server.repositories.ApplicationRepository;
import com.cartobucket.auth.rpc.server.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.rpc.server.repositories.EventRepository;
import com.cartobucket.auth.rpc.server.repositories.ProfileRepository;
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
    final ScopeService scopeService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ApplicationSecretRepository applicationSecretRepository,
            EventRepository eventRepository,
            ProfileRepository profileRepository,
            ScopeService scopeService
    ) {
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
        this.eventRepository = eventRepository;
        this.profileRepository = profileRepository;
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
                Pair.create(ApplicationMapper.from(application), ProfileMapper.from(profile)),
                EventType.DELETE
        );
    }

    @Override
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
        return Pair.create(application, profile);
    }

    @Override
    @Transactional
    public Pair<Application, Profile> createApplication(final Application application, final Profile profile) {
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

        var applicationProfilePair = Pair.create(ApplicationMapper.from(_application), ProfileMapper.from(_profile));
        eventRepository.createApplicationProfileEvent(applicationProfilePair, EventType.CREATE);
        return applicationProfilePair;
    }

    @Override
    public List<ApplicationSecret> getApplicationSecrets(final List<UUID> applicationIds) throws ApplicationNotFound {
        return applicationSecretRepository.findByApplicationIdIn(applicationIds)
                .stream()
                .map(ApplicationSecretMapper::from)
                .toList();
    }

    @Override
    @Transactional
    public ApplicationSecret createApplicationSecret(
            final ApplicationSecret applicationSecret) throws ApplicationNotFound {
        final var application = applicationRepository
                .findByIdOptional(applicationSecret.getApplicationId())
                .orElseThrow(ApplicationNotFound::new);

        var scopes = scopeService.filterScopesForAuthorizationServerId(
                application.getAuthorizationServerId(),
                ScopeService.scopeListToScopeString(applicationSecret.getScopes()));

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
            applicationSecret.setScopes(scopes);
            applicationSecret.setUpdatedOn(OffsetDateTime.now());
            applicationSecret.setCreatedOn(OffsetDateTime.now());
            applicationSecretRepository.persist(
                    ApplicationSecretMapper.to(applicationSecret)
            );
            return applicationSecret;
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
    public List<Application> getApplications(final List<UUID> authorizationServerIds) {
        if (authorizationServerIds.isEmpty()) {
            return applicationRepository
                    .listAll()
                    .stream()
                    .map(ApplicationMapper::from)
                    .toList();
        } else {
            return applicationRepository
                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                    .stream()
                    .map(ApplicationMapper::from)
                    .toList();
        }
    }

    @Override
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
