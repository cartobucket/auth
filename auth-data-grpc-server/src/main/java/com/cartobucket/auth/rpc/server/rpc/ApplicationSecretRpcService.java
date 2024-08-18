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
import com.cartobucket.auth.rpc.ApplicationSecretCreateRequest;
import com.cartobucket.auth.rpc.ApplicationSecretCreateResponse;
import com.cartobucket.auth.rpc.ApplicationSecretDeleteRequest;
import com.cartobucket.auth.rpc.ApplicationSecretListRequest;
import com.cartobucket.auth.rpc.ApplicationSecretListResponse;
import com.cartobucket.auth.rpc.ApplicationSecretResponse;
import com.cartobucket.auth.rpc.ApplicationSecrets;
import com.cartobucket.auth.rpc.IsApplicationSecretValidRequest;
import com.cartobucket.auth.rpc.IsApplicationSecretValidResponse;
import com.cartobucket.auth.data.services.grpc.mappers.server.ScopeMapper;
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
        var applicationSecret = applicationService.createApplicationSecret(
                new ApplicationSecret.Builder()
                        .setApplicationId(UUID.fromString(request.getApplicationId()))
                        .setScopes(
                                request
                                        .getScopesList()
                                        .stream()
                                        .map(
                                                scope -> new com.cartobucket.auth.data.domain.Scope(UUID.fromString(scope.getId()))
                                        ).toList()
                        )
                        .setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()))
                        .setName(request.getName())
                        .setExpiresIn(request.getExpiresIn())
                        .build()
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
                                .addAllScopes(applicationSecret
                                        .getScopes()
                                        .stream()
                                        .map(ScopeMapper::toResponse)
                                        .toList()
                                )
                                .setAuthorizationServerId(String.valueOf(applicationSecret.getAuthorizationServerId()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(applicationSecret.getCreatedOn().toEpochSecond()).build())
                                .setExpiresIn(applicationSecret.getExpiresIn())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ApplicationSecretListResponse> listApplicationSecrets(ApplicationSecretListRequest request) {
        final var applicationSecrets = applicationService.getApplicationSecrets(
                request
                        .getApplicationIdList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
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
                                                                .addAllScopes(applicationSecret
                                                                        .getScopes()
                                                                        .stream()
                                                                        .map(ScopeMapper::toResponse)
                                                                        .toList()
                                                                )
                                                                .setAuthorizationServerId(String.valueOf(applicationSecret.getAuthorizationServerId()))
                                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(applicationSecret.getCreatedOn().toEpochSecond()).build())
                                                                .setExpiresIn(applicationSecret.getExpiresIn())
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

    @Override
    public Uni<IsApplicationSecretValidResponse> isApplicationSecretValid(IsApplicationSecretValidRequest request) {
        return Uni
                .createFrom()
                .item(
                    IsApplicationSecretValidResponse
                            .newBuilder()
                            .setIsValid(
                                    applicationService.isApplicationSecretValid(
                                            UUID.fromString(request.getAuthorizationServerId()),
                                            UUID.fromString(request.getApplicationId()),
                                            request.getApplicationSecret()
                                    )
                            )
                            .build()
                );
    }
}
