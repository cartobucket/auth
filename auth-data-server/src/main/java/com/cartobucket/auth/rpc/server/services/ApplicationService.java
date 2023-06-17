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

import com.cartobucket.auth.rpc.server.entities.mappers.ApplicationMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ApplicationSecretMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.rpc.server.repositories.ApplicationRepository;
import com.cartobucket.auth.rpc.server.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.rpc.server.repositories.ProfileRepository;
import com.cartobucket.auth.data.services.ScopeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.graalvm.collections.Pair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class ApplicationService implements com.cartobucket.auth.data.services.ApplicationService {
    final ApplicationRepository applicationRepository;
    final ApplicationSecretRepository applicationSecretRepository;
    final ProfileRepository profileRepository;
    final ScopeService scopeService;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository, ProfileRepository profileRepository, ScopeService scopeService) {
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
        this.profileRepository = profileRepository;
        this.scopeService = scopeService;
    }

    @Override
    @Transactional
    public void deleteApplication(final UUID applicationId) throws ApplicationNotFound {
        applicationRepository.delete(
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(ApplicationNotFound::new)
        );
    }

    @Override
    public Pair<Application, Profile> getApplication(final UUID applicationId)
            throws ApplicationNotFound, ProfileNotFound {
        final var application = applicationRepository
                .findById(applicationId)
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
        var _application = ApplicationMapper.from(applicationRepository.save(ApplicationMapper.to(application)));

        profile.setResource(_application.getId());
        profile.setAuthorizationServerId(_application.getAuthorizationServerId());
        profile.setProfileType(ProfileType.Application);
        var _profile = ProfileMapper.from(profileRepository.save(ProfileMapper.to(profile)));

        return Pair.create(_application, _profile);
    }

    @Override
    public List<ApplicationSecret> getApplicationSecrets(final UUID applicationId) throws ApplicationNotFound {
        final var application = applicationRepository
                .findById(applicationId)
                .orElseThrow(ApplicationNotFound::new);

        return applicationSecretRepository.findByApplicationId(application.getId())
                .stream()
                .map(ApplicationSecretMapper::from)
                .toList();
    }

    @Override
    @Transactional
    public ApplicationSecret createApplicationSecret(
            final UUID applicationId,
            final ApplicationSecret applicationSecret) throws ApplicationNotFound {
        final var application = applicationRepository
                .findById(applicationId)
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
            return ApplicationSecretMapper.from(
                    applicationSecretRepository.save(
                            ApplicationSecretMapper.to(applicationSecret)
                    )
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteApplicationSecret(final UUID applicationId, final UUID secretId)
            throws ApplicationSecretNoApplicationBadData,
            ApplicationSecretNotFound,
            ApplicationNotFound {
        final var application = applicationRepository
                .findById(applicationId)
                .orElseThrow(ApplicationNotFound::new);

        final var secret = applicationSecretRepository
                .findById(secretId)
                .orElseThrow(ApplicationSecretNotFound::new);
        if (!secret.getApplicationId().equals(application.getId())) {
            throw new ApplicationSecretNoApplicationBadData();
        }
        applicationSecretRepository.delete(secret);
    }

    @Override
    public List<Application> getApplications(final List<UUID> authorizationServerIds) {
        if (authorizationServerIds.isEmpty()) {
            return StreamSupport
                    .stream(applicationRepository.findAll().spliterator(), false)
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

}
