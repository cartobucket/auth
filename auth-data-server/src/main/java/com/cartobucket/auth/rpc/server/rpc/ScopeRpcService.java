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

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound;
import com.cartobucket.auth.rpc.Scopes;
import com.cartobucket.auth.data.services.ScopeService;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;

import java.util.UUID;

@GrpcService
public class ScopeRpcService implements Scopes {
    final ScopeService scopeService;

    public ScopeRpcService(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Override
    @Transactional
    @Blocking
    public Uni<com.cartobucket.auth.rpc.ScopeCreateResponse> createScope(com.cartobucket.auth.rpc.ScopeCreateRequest request) {
        var scope = new Scope();
        scope.setName(request.getName());
        scope.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        scope = scopeService.createScope(scope);

        var response = com.cartobucket.auth.rpc.ScopeCreateResponse
                .newBuilder()
                .setId(String.valueOf(scope.getId()))
                .setName(scope.getName())
                .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServerId()))
                .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                .build();

        return Uni
                .createFrom()
                .item(response);
    }

    @Override
    @Blocking
    public Uni<com.cartobucket.auth.rpc.ScopesListResponse> listScopes(com.cartobucket.auth.rpc.ScopeListRequest request) {
        final var scopes = scopeService.getScopes(
                request.getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
        );

        var response = com.cartobucket.auth.rpc.ScopesListResponse
                .newBuilder()
                .setLimit(0)
                .setOffset(0);
        for (var scope : scopes) {
            response.addScopes(com.cartobucket.auth.rpc.ScopeResponse
                    .newBuilder()
                    .setId(String.valueOf(scope.getId()))
                    .setName(scope.getName())
                    .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServerId()))
                    .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
            );
        }

        return Uni.createFrom().item(response.build());
    }

    @Override
    @Blocking
    public Uni<com.cartobucket.auth.rpc.ScopeResponse> deleteScope(com.cartobucket.auth.rpc.ScopeDeleteRequest request) {
        final var scopeId = UUID.fromString(request.getAuthorizationServerId());
        try {
            scopeService.deleteScope(scopeId);
            return Uni.createFrom().item(
                    com.cartobucket.auth.rpc.ScopeResponse
                            .newBuilder()
                            .setId(String.valueOf(scopeId))
                            .build()
            );
        } catch (ScopeNotFound e) {
            throw new RuntimeException(e);
        }
    }
}