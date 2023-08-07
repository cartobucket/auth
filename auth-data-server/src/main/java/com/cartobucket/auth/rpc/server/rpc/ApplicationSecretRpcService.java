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

package com.cartobucket.auth.rpc.server.rpc;


import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.rpc.ApplicationSecretCreateRequest;
import com.cartobucket.auth.rpc.ApplicationSecretCreateResponse;
import com.cartobucket.auth.rpc.ApplicationSecretDeleteRequest;
import com.cartobucket.auth.rpc.ApplicationSecretListRequest;
import com.cartobucket.auth.rpc.ApplicationSecretListResponse;
import com.cartobucket.auth.rpc.ApplicationSecretResponse;
import com.cartobucket.auth.rpc.ApplicationSecrets;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

@GrpcService
public class ApplicationSecretRpcService implements ApplicationSecrets {
    final ApplicationService applicationService;

    public ApplicationSecretRpcService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    @Blocking
    public Uni<ApplicationSecretCreateResponse> createApplicationSecret(ApplicationSecretCreateRequest request) {
        var applicationSecret = new ApplicationSecret();
        applicationSecret.setApplicationId(UUID.fromString(request.getApplicationId()));
        applicationSecret.setScopes(ScopeService.scopeStringToScopeList(request.getScopes()));
        applicationSecret.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        applicationSecret.setName(request.getName());
        applicationSecret = applicationService.createApplicationSecret(
                UUID.fromString(request.getApplicationId()),
                applicationSecret
        );
        return Uni
                .createFrom()
                .item(
                        ApplicationSecretCreateResponse
                                .newBuilder()
                                .setId(String.valueOf(applicationSecret.getId()))
                                .setApplicationId(String.valueOf(applicationSecret.getApplicationId()))
                                .setApplicationSecret(applicationSecret.getApplicationSecret())
                                .setName(applicationSecret.getName())
                                .setScopes(ScopeService.scopeListToScopeString(applicationSecret.getScopes()))
                                .setAuthorizationServerId(String.valueOf(applicationSecret.getAuthorizationServerId()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(applicationSecret.getCreatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationSecretListResponse> listApplicationSecrets(ApplicationSecretListRequest request) {
        final var applicationSecrets = applicationService.getApplicationSecrets(
                UUID.fromString(request.getApplicationId())
        );
        return Uni
                .createFrom()
                .item(
                        ApplicationSecretListResponse
                                .newBuilder()
                                .addAllApplicationSecrets(
                                        applicationSecrets
                                                .stream()
                                                .map(
                                                        applicationSecret -> ApplicationSecretResponse
                                                                .newBuilder()
                                                                .setId(String.valueOf(applicationSecret.getId()))
                                                                .setApplicationId(String.valueOf(applicationSecret.getApplicationId()))
                                                                .setName(applicationSecret.getName())
                                                                .setScopes(ScopeService.scopeListToScopeString(applicationSecret.getScopes()))
                                                                .setAuthorizationServerId(String.valueOf(applicationSecret.getAuthorizationServerId()))
                                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(applicationSecret.getCreatedOn().toEpochSecond()).build())
                                                                .build()
                                                )
                                                .toList())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationSecretResponse> deleteApplicationSecret(ApplicationSecretDeleteRequest request) {
        applicationService.deleteApplicationSecret(
                UUID.fromString(request.getApplicationId()),
                UUID.fromString(request.getId())
        );
        return Uni
                .createFrom()
                .item(
                        ApplicationSecretResponse
                                .newBuilder()
                                .setId(request.getId())
                                .setApplicationId(request.getApplicationId())
                                .build()
                );
    }
}
