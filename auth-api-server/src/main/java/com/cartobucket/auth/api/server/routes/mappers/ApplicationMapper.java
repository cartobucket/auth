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

import com.cartobucket.auth.model.generated.ApplicationRequest;
import com.cartobucket.auth.model.generated.ApplicationResponse;
import com.cartobucket.auth.model.generated.ApplicationSecretRequest;
import com.cartobucket.auth.model.generated.ApplicationSecretResponse;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ScopeService;

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