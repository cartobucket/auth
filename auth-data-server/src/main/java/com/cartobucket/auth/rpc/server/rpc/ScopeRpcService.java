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

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.rpc.ScopeCreateRequest;
import com.cartobucket.auth.data.rpc.ScopeDeleteRequest;
import com.cartobucket.auth.data.rpc.ScopeGetRequest;
import com.cartobucket.auth.data.rpc.ScopeListRequest;
import com.cartobucket.auth.data.rpc.Scopes;
import com.cartobucket.auth.data.rpc.ScopesListResponse;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.rpc.server.entities.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.data.services.impls.mappers.MetadataMapper;
import com.cartobucket.auth.rpc.server.repositories.AuthorizationServerRepository;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

@GrpcService
public class ScopeRpcService implements Scopes {
    final ScopeService scopeService;
    final AuthorizationServerRepository authorizationRepository;

    public ScopeRpcService(ScopeService scopeService, AuthorizationServerRepository authorizationRepository) {
        this.scopeService = scopeService;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    @Blocking
    public Uni<com.cartobucket.auth.data.rpc.Scope> createScope(ScopeCreateRequest request) {
        var scope = new Scope();
        scope.setName(request.getName());
        scope.setAuthorizationServer(
                AuthorizationServerMapper.from(
                        authorizationRepository.findById(
                                UUID.fromString(
                                        request.getAuthorizationServerId()
                                )
                        )
                )
        );
        scope.setMetadata(MetadataMapper.from(request.getMetadata()));
        scope = scopeService.createScope(scope);

        var response = com.cartobucket.auth.data.rpc.Scope
                .newBuilder()
                .setId(String.valueOf(scope.getId()))
                .setName(scope.getName())
                .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                .setMetadata(MetadataMapper.to(scope.getMetadata()))
                .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                .build();

        return Uni
                .createFrom()
                .item(response);
    }

    @Override
    @Blocking
    public Uni<ScopesListResponse> listScopes(ScopeListRequest request) {
        final var scopes = scopeService.getScopes(
                request.getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList(),
                new Page(
                        Long.valueOf(request.getLimit()).intValue(),
                        Long.valueOf(request.getOffset()).intValue()
                )
        );

        var response = ScopesListResponse
                .newBuilder()
                .setLimit(0)
                .setOffset(0)
                .addAllScopes(
                        scopes.stream()
                                .map(scope -> com.cartobucket.auth.data.rpc.Scope
                                        .newBuilder()
                                        .setId(String.valueOf(scope.getId()))
                                        .setName(scope.getName())
                                        .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                                        .setMetadata(MetadataMapper.to(scope.getMetadata()))
                                        .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                                        .build()
                                )
                                .toList()
                );

        return Uni.createFrom().item(response.build());
    }

    @Override
    @Blocking
    public Uni<com.cartobucket.auth.data.rpc.Scope> deleteScope(ScopeDeleteRequest request) {
        final var scopeId = UUID.fromString(request.getId());
        scopeService.deleteScope(scopeId);
        return Uni.createFrom().item(
                com.cartobucket.auth.data.rpc.Scope
                        .newBuilder()
                        .setId(String.valueOf(scopeId))
                        .build()
        );
    }

    @Override
    @Blocking
    public Uni<com.cartobucket.auth.data.rpc.Scope> getScope(ScopeGetRequest request) {
        final var scope = scopeService.getScope(UUID.fromString(request.getId()));

        return Uni
                .createFrom()
                .item(com.cartobucket.auth.data.rpc.Scope
                        .newBuilder()
                        .setId(String.valueOf(scope.getId()))
                        .setName(scope.getName())
                        .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer()))
                        .setMetadata(MetadataMapper.to(scope.getMetadata()))
                        .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                        .build());
    }
}
