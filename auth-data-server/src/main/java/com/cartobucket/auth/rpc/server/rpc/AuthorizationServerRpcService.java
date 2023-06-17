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

import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.rpc.AuthorizationServerCreateRequest;
import com.cartobucket.auth.rpc.AuthorizationServerCreateResponse;
import com.cartobucket.auth.rpc.AuthorizationServerDeleteRequest;
import com.cartobucket.auth.rpc.AuthorizationServerListRequest;
import com.cartobucket.auth.rpc.AuthorizationServerResponse;
import com.cartobucket.auth.rpc.AuthorizationServerUpdateRequest;
import com.cartobucket.auth.rpc.AuthorizationServers;
import com.cartobucket.auth.rpc.AuthorizationServersListResponse;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@GrpcService
public class AuthorizationServerRpcService implements AuthorizationServers {
    final AuthorizationServerService authorizationServerService;

    public AuthorizationServerRpcService(AuthorizationServerService scopeService) {
        this.authorizationServerService = scopeService;
    }


    @Override
    @Blocking
    public Uni<AuthorizationServerCreateResponse> createAuthorizationServer(AuthorizationServerCreateRequest request) {
        try {
            var authorizationServer = new AuthorizationServer();
            authorizationServer.setName(request.getName());
            authorizationServer.setAudience(request.getAudience());
            authorizationServer.setServerUrl(new URL(request.getServerUrl()));
            authorizationServer.setAuthorizationCodeTokenExpiration(request.getAuthorizationCodeTokenExpiration());
            authorizationServer.setClientCredentialsTokenExpiration(request.getClientCredentialsTokenExpiration());
            authorizationServer = authorizationServerService.createAuthorizationServer(authorizationServer);

            return Uni
                    .createFrom()
                    .item(
                            AuthorizationServerCreateResponse
                                    .newBuilder()
                                    .setId(String.valueOf(authorizationServer.getId()))
                                    .setName(authorizationServer.getName())
                                    .setAudience(authorizationServer.getAudience())
                                    .setServerUrl(String.valueOf(authorizationServer.getServerUrl()))
                                    .setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration())
                                    .setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration())
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                    .build()
                    );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Blocking
    public Uni<AuthorizationServersListResponse> listAuthorizationServers(AuthorizationServerListRequest request) {
        final var authorizationServers = authorizationServerService.getAuthorizationServers();
        var response = AuthorizationServersListResponse.newBuilder();
        response.addAllAuthorizationServers(
                authorizationServers
                        .stream()
                        .map(authorizationServer -> AuthorizationServerResponse
                                .newBuilder()
                                .setId(String.valueOf(authorizationServer.getId()))
                                .setName(authorizationServer.getName())
                                .setAudience(authorizationServer.getAudience())
                                .setServerUrl(String.valueOf(authorizationServer.getServerUrl()))
                                .setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration())
                                .setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration())
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                .build())
                        .toList()
        );
        return Uni.createFrom().item(response.build());
    }

    @Override
    @Blocking
    public Uni<AuthorizationServerResponse> deleteAuthorizationServer(AuthorizationServerDeleteRequest request) {
        final var authorizationServerId = UUID.fromString(request.getAuthorizationServerId());
        authorizationServerService.deleteAuthorizationServer(authorizationServerId);
        return Uni.createFrom().item(AuthorizationServerResponse.newBuilder().setId(request.getAuthorizationServerId()).build());
    }

    @Override
    @Blocking
    public Uni<AuthorizationServerResponse> updateAuthorizationServer(AuthorizationServerUpdateRequest request) {
        try{
            var authorizationServer = authorizationServerService.getAuthorizationServer(UUID.fromString(request.getId()));
            authorizationServer.setName(request.getName());
            authorizationServer.setAudience(request.getAudience());
            authorizationServer.setServerUrl(new URL(request.getServerUrl()));
            authorizationServer.setAuthorizationCodeTokenExpiration(request.getAuthorizationCodeTokenExpiration());
            authorizationServer.setClientCredentialsTokenExpiration(request.getClientCredentialsTokenExpiration());
            authorizationServer = authorizationServerService.updateAuthorizationServer(UUID.fromString(request.getId()), authorizationServer);

            return Uni
                    .createFrom()
                    .item(
                            AuthorizationServerResponse
                                    .newBuilder()
                                    .setId(String.valueOf(authorizationServer.getId()))
                                    .setName(authorizationServer.getName())
                                    .setAudience(authorizationServer.getAudience())
                                    .setServerUrl(String.valueOf(authorizationServer.getServerUrl()))
                                    .setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration())
                                    .setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration())
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                    .build()
                    );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
