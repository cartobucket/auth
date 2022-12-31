package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.ApplicationSecret;
import com.cartobucket.auth.models.Scope;
import com.cartobucket.auth.repositories.ApplicationRepository;
import com.cartobucket.auth.repositories.ApplicationSecretRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ApplicationServiceImpl implements com.cartobucket.auth.services.ApplicationService {
    final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    final ApplicationRepository applicationRepository;
    final ApplicationSecretRepository applicationSecretRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationSecretRepository applicationSecretRepository) {
        this.applicationRepository = applicationRepository;
        this.applicationSecretRepository = applicationSecretRepository;
    }

    @Override
    public ApplicationSecret createApplicationSecret(UUID applicationId, String name, List<Scope> scopes, Long expiration) {
        var clientSecret = Base64.getEncoder().encodeToString(SecureRandom.getSeed(128));
        var clientSecretHash = bCryptPasswordEncoder.encode(clientSecret);

        var applicationToken = new ApplicationSecret();
        applicationToken.setName(name);
        applicationToken.setApplicationId(applicationId);
        applicationToken.setClientSecret(clientSecret);
        applicationToken.setClientSecretHash(clientSecretHash);
        applicationToken.setScopes(scopes);

        var savedToken = applicationSecretRepository.save(applicationToken);

        return savedToken;
    }

    @Override
    public ApplicationSecret revokeApplicationSecret(UUID applicationSecretId) {
        return null;
    }

    @Override
    public Application getApplicationFromClientCredentials(String clientId, String clientSecret) {
        var application = applicationRepository.findByClientId(clientId);
        var applicationTokens = applicationSecretRepository.findByApplicationId(application.getId());
        for (var applicationToken : applicationTokens) {
            if (bCryptPasswordEncoder.matches(clientSecret, applicationToken.getClientSecretHash())) {
                return application;
            }
        }

        return null;
    }
}
