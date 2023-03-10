package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.ApplicationSecret;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationService {
    ApplicationSecret getApplicationSecretFromClientCredentials(String clientId, String clientSecret);

    void deleteApplication(UUID applicationId);

    ApplicationResponse getApplication(UUID applicationId);

    ApplicationResponse createApplication(ApplicationRequest applicationRequest);

    ApplicationSecretsResponse getApplicationSecrets(UUID applicationId);

    ApplicationSecretResponse createApplicationSecret(UUID applicationId, ApplicationSecretRequest applicationSecretRequest);

    void deleteApplicationSecret(UUID applicationId, UUID secretId);

    ApplicationsResponse getApplications(ApplicationRequestFilter filter);

}
