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

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.model.generated.ApplicationRequest;
import com.cartobucket.auth.model.generated.ApplicationResponse;
import com.cartobucket.auth.model.generated.ApplicationSecretRequest;
import com.cartobucket.auth.model.generated.ApplicationSecretResponse;

public class ApplicationMapper {
    public static Application from(ApplicationRequest applicationRequest) {
        var application = new Application();
        application.setName(applicationRequest.getName());
        application.setAuthorizationServerId(applicationRequest.getAuthorizationServerId());
        if (applicationRequest.getClientId() != null) {
            application.setClientId(applicationRequest.getClientId());
        }
        application.setScopes(
                applicationRequest
                        .getScopes()
                        .stream()
                        .map(scopeId -> {
                            var scope = new Scope();
                            scope.setId(scopeId);
                            return scope;
                        })
                        .toList()
        );
        application.setMetadata(MetadataMapper.from(applicationRequest.getMetadata()));
        return application;
    }

    public static ApplicationResponse toResponse(Application application) {
        var applicationResponse = new ApplicationResponse();
        applicationResponse.setId(String.valueOf(application.getId()));
        applicationResponse.setName(application.getName());
        applicationResponse.setClientId(application.getClientId());
        applicationResponse.setAuthorizationServerId(application.getAuthorizationServerId());
        applicationResponse.setMetadata(MetadataMapper.to(application.getMetadata()));
        applicationResponse.setCreatedOn(application.getCreatedOn());
        applicationResponse.setUpdatedOn(application.getUpdatedOn());
        return applicationResponse;
    }

    public static ApplicationResponse toResponseWithProfile(Application application, Profile profile) {
        var applicationResponse = new ApplicationResponse();
        applicationResponse.setId(String.valueOf(application.getId()));
        applicationResponse.setName(application.getName());
        applicationResponse.setClientId(application.getClientId());
        applicationResponse.setAuthorizationServerId(application.getAuthorizationServerId());
        applicationResponse.setMetadata(MetadataMapper.to(application.getMetadata()));
        applicationResponse.setCreatedOn(application.getCreatedOn());
        applicationResponse.setUpdatedOn(application.getUpdatedOn());
        applicationResponse.setProfile(profile.getProfile());
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

    public static ApplicationSecretResponse toSecretResponse(ApplicationSecret applicationSecret) {
        var secretResponse = new ApplicationSecretResponse();
        secretResponse.setClientSecret(applicationSecret.getApplicationSecret());
        secretResponse.setId(applicationSecret.getId());
        secretResponse.setName(applicationSecret.getName());
        secretResponse.setApplicationId(applicationSecret.getApplicationId());
        secretResponse.setAuthorizationServerId(applicationSecret.getAuthorizationServerId());
        return secretResponse;
    }
}
