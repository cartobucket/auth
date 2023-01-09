package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.mappers.ApplicationMapper;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.services.ApplicationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class ApplicationServiceImpl implements ApplicationService {
    final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    final ApplicationRepository applicationRepository;
    final ApplicationSecretRepository applicationSecretRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
    }

    @Override
    public Application getApplicationFromClientCredentials(String clientId, String clientSecret) {
        final var application = applicationRepository.findByClientId(clientId);
        if (application == null) {
            throw new BadRequestException("Unable to find the Application with the credentials provided");
        }

        var applicationTokens = applicationSecretRepository.findByApplicationId(application.getId());
        for (var applicationToken : applicationTokens) {
            if (bCryptPasswordEncoder.matches(clientSecret, applicationToken.getApplicationSecretHash())) {
                return application;
            }
        }

        throw new BadRequestException("Unable to find the Application with the credentials provided");
    }

    @Override
    public void deleteApplication(UUID applicationId) {
        final var application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new NotFoundException();
        }
        applicationRepository.delete(application.get());
    }

    @Override
    public ApplicationResponse getApplication(UUID applicationId) {
        final var application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new NotFoundException();
        }
        return ApplicationMapper.toResponse(application.get());
    }

    @Override
    public ApplicationResponse createApplication(ApplicationRequest applicationRequest) {
        var application = ApplicationMapper.from(applicationRequest);
        application.setCreatedOn(OffsetDateTime.now());
        application.setUpdatedOn(OffsetDateTime.now());
        application = applicationRepository.save(application);
        return ApplicationMapper.toResponse(application);
    }

    @Override
    public ApplicationSecretsResponse getApplicationSecrets(UUID applicationId) {
        final var application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new NotFoundException();
        }
        var secrets = applicationSecretRepository.findByApplicationId(applicationId)
                .stream()
                .map(ApplicationMapper::toSecretResponse)
                .toList();
        var applicationSecretsResponse = new ApplicationSecretsResponse();
        applicationSecretsResponse.setApplicationSecrets(secrets);
        return applicationSecretsResponse;
    }

    @Override
    public ApplicationSecretResponse createApplicationSecret(
            UUID applicationId,
            ApplicationSecretRequest applicationSecretRequest) {
        final var application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new NotFoundException();
        }
        var applicationSecret = ApplicationMapper.secretFrom(
                application.get(),
                applicationSecretRequest);
        final var secret = Base64.getEncoder().encodeToString(SecureRandom.getSeed(128));
        final var secretHash = bCryptPasswordEncoder.encode(secret);
        applicationSecret.setApplicationSecret(secret);
        applicationSecret.setApplicationSecretHash(secretHash);
        applicationSecret.setUpdatedOn(OffsetDateTime.now());
        applicationSecret = applicationSecretRepository.save(applicationSecret);
        return ApplicationMapper.toSecretResponse(applicationSecret);
    }

    @Override
    public void deleteApplicationSecret(UUID applicationId, UUID secretId) {
        final var application = applicationRepository.findById(applicationId);
        if (application.isEmpty()) {
            throw new NotFoundException();
        }
        final var secret = applicationSecretRepository.findById(secretId);
        if (secret.isEmpty()) {
            throw new NotFoundException();
        }
        if (!secret.get().getApplicationId().equals(application.get().getId())) {
            throw new BadRequestException();
        }
        applicationSecretRepository.delete(secret.get());
    }

    @Override
    public ApplicationsResponse getApplications() {
        var applications = StreamSupport
                .stream(applicationRepository.findAll().spliterator(), false)
                .map(ApplicationMapper::toResponse)
                .toList();
        var applicationsResponse = new ApplicationsResponse();
        applicationsResponse.setApplications(applications);

        return applicationsResponse;
    }
}
