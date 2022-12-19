package com.cartobucket.auth.services;

import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.ApplicationSecret;
import com.cartobucket.auth.models.Scope;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    ApplicationSecret createApplicationSecret(UUID applicationId, String name, List<Scope> scopes, Long expiration);
    ApplicationSecret revokeApplicationSecret(UUID applicationSecretId);

    Application getApplicationFromClientCredentials(String clientId, String clientSecret);
}
