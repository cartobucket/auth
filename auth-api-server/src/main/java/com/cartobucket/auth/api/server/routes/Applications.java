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
import com.cartobucket.auth.api.server.routes.mappers.ProfileMapper;
import com.cartobucket.auth.generated.ApplicationsApi;
import com.cartobucket.auth.model.generated.ApplicationRequest;
import com.cartobucket.auth.model.generated.ApplicationSecretRequest;
import com.cartobucket.auth.model.generated.ApplicationSecretsResponse;
import com.cartobucket.auth.model.generated.ApplicationsResponse;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response createApplication(ApplicationRequest applicationRequest) {
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toResponse(
                                applicationService
                                        .createApplication(
                                                ApplicationMapper.from(applicationRequest),
                                                ProfileMapper.toProfile((Map<String, Object>) applicationRequest.getProfile())
                                        ).getLeft()
                        )
                )
                .build();
    }

    @Override
    public Response createApplicationSecret(UUID applicationId, ApplicationSecretRequest applicationSecretRequest) {
        final var application = applicationService.getApplication(applicationId);
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toSecretResponse(
                            applicationService
                                    .createApplicationSecret(
                                            applicationId,
                                            ApplicationMapper.secretFrom(application.getLeft(), applicationSecretRequest)
                                    )
                    )
                )
                .build();
    }

    @Override
    public Response deleteApplication(UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return Response
                .ok()
                .build();
    }

    @Override
    public Response deleteApplicationSecret(UUID applicationId, UUID secretId) {
        applicationService.deleteApplicationSecret(applicationId, secretId);
        return Response
                .ok()
                .build();
    }

    @Override
    public Response getApplication(UUID applicationId) {
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toResponse(
                            applicationService.getApplication(applicationId).getLeft()
                    )
                )
                .build();
    }

    @Override
    public Response listApplicationSecrets(UUID applicationId) {
        final var applicationSecretsResponse = new ApplicationSecretsResponse();
        applicationSecretsResponse.setApplicationSecrets(
                applicationService.getApplicationSecrets(applicationId)
                        .stream()
                        .map(ApplicationMapper::toSecretResponse)
                        .collect(Collectors.toList())
        );

        return Response
                .ok()
                .entity(applicationSecretsResponse)
                .build();
    }

    @Override
    public Response listApplications(List<UUID> authorizationServerIds) {
        final var applicationsResponse = new ApplicationsResponse();
        applicationsResponse.setApplications(
                applicationService
                        .getApplications(
                                authorizationServerIds
                        )
                        .stream()
                        .map(ApplicationMapper::toResponse)
                        .collect(Collectors.toList())
        );
        return Response
                .ok()
                .entity(applicationsResponse)
                .build();
    }
}