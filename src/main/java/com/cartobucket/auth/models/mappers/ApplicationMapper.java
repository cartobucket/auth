package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.ApplicationRequest;
import com.cartobucket.auth.model.generated.ApplicationResponse;
import com.cartobucket.auth.model.generated.ApplicationSecretRequest;
import com.cartobucket.auth.model.generated.ApplicationSecretResponse;
import com.cartobucket.auth.models.Application;
import com.cartobucket.auth.models.ApplicationSecret;
import com.cartobucket.auth.models.Scope;
import com.cartobucket.auth.services.ScopeService;

import java.util.UUID;

public class ApplicationMapper {
    public static Application from(ApplicationRequest applicationRequest) {
        var application = new Application();
        application.setName(applicationRequest.getName());
        application.setAuthorizationServerId(applicationRequest.getAuthorizationServerId());
        if (applicationRequest.getClientId() != null) {
            application.setClientId(applicationRequest.getClientId());
        } else {
            var id = UUID.randomUUID();
            application.setId(id);
            application.setClientId(String.valueOf(id));
        }
        return application;
    }
    public static ApplicationResponse toResponse(Application application) {
        var applicationResponse = new ApplicationResponse();
        applicationResponse.setId(String.valueOf(application.getId()));
        applicationResponse.setName(application.getName());
        applicationResponse.setClientId(application.getClientId());
        applicationResponse.setAuthorizationServerId(application.getAuthorizationServerId());
        applicationResponse.setCreatedOn(application.getCreatedOn());
        applicationResponse.setUpdatedOn(application.getUpdatedOn());
        return applicationResponse;
    }

    public static ApplicationSecret secretFrom(Application application, ApplicationSecretRequest applicationSecretRequest) {
        var secret = new ApplicationSecret();
        secret.setApplicationId(application.getId());
        secret.setName(applicationSecretRequest.getName());
        secret.setAuthorizationServerId(application.getAuthorizationServerId());
        if (applicationSecretRequest.getScopes() != null)
            secret.setScopes(ScopeService.scopeStringToScopeList(applicationSecretRequest.getScopes()));
        return secret;
    }

    // TODO: This is temporary as we don't have the scope in the db yet.
    public static Scope fromName(String name) {
        var scope = new Scope();
        scope.setName(name);
        return scope;
    }

    public static ApplicationSecretResponse toSecretResponse(ApplicationSecret applicationSecret) {
        var secretResponse = new ApplicationSecretResponse();
        secretResponse.setClientSecret(applicationSecret.getApplicationSecret());
        secretResponse.setId(applicationSecret.getId());
        secretResponse.setName(applicationSecret.getName());
        return secretResponse;
    }
}
