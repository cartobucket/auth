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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
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
        try {
            var messageDigest = MessageDigest.getInstance("SHA-256");
            var secretHash = new BigInteger(
                    1, messageDigest
                    .digest(clientSecret.getBytes()))
                    .toString(16);


            var applicationSecret = applicationSecretRepository.findByApplicationSecretHash(secretHash);
            if (applicationSecret.isEmpty()) {
                throw new NotFoundException();
            }
            return application;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
            applicationSecret.setUpdatedOn(OffsetDateTime.now());
            applicationSecret = applicationSecretRepository.save(applicationSecret);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

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
