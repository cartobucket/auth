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
package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.api.server.routes.mappers.ApplicationMapper;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.api.interfaces.ApplicationSecretsApi;
import com.cartobucket.auth.api.dto.ApplicationSecretRequest;
import com.cartobucket.auth.api.dto.ApplicationSecretsResponse;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ApplicationSecrets implements ApplicationSecretsApi {
    final ApplicationService applicationService;

    public ApplicationSecrets(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public Response createApplicationSecret(ApplicationSecretRequest applicationSecretRequest) {
        final var application = applicationService.getApplication(applicationSecretRequest.getApplicationId());
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toSecretResponse(
                                applicationService
                                        .createApplicationSecret(
                                                ApplicationMapper.secretFrom(application.getLeft(), applicationSecretRequest)
                                        )
                        )
                )
                .build();
    }

    @Override
    public Response deleteApplicationSecret(UUID secretId) {
        applicationService.deleteApplicationSecret(secretId);
        return Response
                .ok()
                .build();
    }

    @Override
    public Response listApplicationSecrets(List<UUID> applicationIds) {
        final var applicationSecretsResponse = new ApplicationSecretsResponse();
        // TODO: This should be able to be blank, this should also take a list of authorization server ids
        applicationSecretsResponse.setApplicationSecrets(
                applicationService.getApplicationSecrets(applicationIds)
                        .stream()
                        .map(ApplicationMapper::toSecretResponse)
                        .collect(Collectors.toList())
        );

        return Response
                .ok()
                .entity(applicationSecretsResponse)
                .build();
    }
}
