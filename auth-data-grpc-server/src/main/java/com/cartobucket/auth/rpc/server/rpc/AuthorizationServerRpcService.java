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
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.data.services.grpc.mappers.ProfileMapper;
import com.cartobucket.auth.rpc.AuthorizationServerCreateRequest;
import com.cartobucket.auth.rpc.AuthorizationServerCreateResponse;
import com.cartobucket.auth.rpc.AuthorizationServerDeleteRequest;
import com.cartobucket.auth.rpc.AuthorizationServerGetRequest;
import com.cartobucket.auth.rpc.AuthorizationServerListRequest;
import com.cartobucket.auth.rpc.AuthorizationServerResponse;
import com.cartobucket.auth.rpc.AuthorizationServerSigningKeyResponse;
import com.cartobucket.auth.rpc.AuthorizationServerUpdateRequest;
import com.cartobucket.auth.rpc.AuthorizationServers;
import com.cartobucket.auth.rpc.AuthorizationServersListResponse;
import com.cartobucket.auth.rpc.GenerateAccessTokenRequest;
import com.cartobucket.auth.rpc.GenerateAccessTokenResponse;
import com.cartobucket.auth.rpc.Jwk;
import com.cartobucket.auth.rpc.JwksResponse;
import com.cartobucket.auth.rpc.ValidateJwtForAuthorizationServerRequest;
import com.cartobucket.auth.rpc.ValidateJwtForAuthorizationServerResponse;
import com.cartobucket.auth.rpc.server.rpc.mappers.MetadataMapper;
import com.cartobucket.auth.rpc.server.rpc.mappers.ScopeMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.net.MalformedURLException;
import java.net.URI;
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
            authorizationServer.setServerUrl(URI.create((request.getServerUrl())).toURL());
            authorizationServer.setAuthorizationCodeTokenExpiration(request.getAuthorizationCodeTokenExpiration());
            authorizationServer.setClientCredentialsTokenExpiration(request.getClientCredentialsTokenExpiration());
            authorizationServer.setMetadata(MetadataMapper.toMetadata(request.getMetadata()));
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
                                    .setMetadata(MetadataMapper.from(authorizationServer.getMetadata()))
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                    .addAllScopes(
                                            authorizationServer
                                                    .getScopes()
                                                    .stream()
                                                    .map(scope -> com.cartobucket.auth.data.rpc.Scope
                                                            .newBuilder()
                                                            .setId(String.valueOf(scope.getId()))
                                                            .setName(scope.getName())
                                                            .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                                                            .setMetadata(MetadataMapper.from(scope.getMetadata()))
                                                            .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                                                            .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                                                            .build()
                                                    )
                                                    .toList()
                                    )
                                    .build()
                    );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Blocking
    public Uni<AuthorizationServersListResponse> listAuthorizationServers(AuthorizationServerListRequest request) {
        final var page = new Page(
                Long.valueOf(request.getLimit()).intValue(),
                Long.valueOf(request.getOffset()).intValue()
        );
        final var authorizationServers = authorizationServerService.getAuthorizationServers(page);
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
                                .setMetadata(MetadataMapper.from(authorizationServer.getMetadata()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                .addAllScopes(
                                        authorizationServer
                                                .getScopes()
                                                .stream()
                                                .map(scope -> com.cartobucket.auth.data.rpc.Scope
                                                        .newBuilder()
                                                        .setId(String.valueOf(scope.getId()))
                                                        .setName(scope.getName())
                                                        .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                                                        .setMetadata(MetadataMapper.from(scope.getMetadata()))
                                                        .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                                                        .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                                                        .build()
                                                )
                                                .toList()
                                )
                                .build())
                        .toList()
        );
        return Uni.createFrom().item(response.build());
    }

    @Override
    @Blocking
    public Uni<AuthorizationServerResponse> deleteAuthorizationServer(AuthorizationServerDeleteRequest request) {
        final var authorizationServerId = UUID.fromString(request.getId());
        authorizationServerService.deleteAuthorizationServer(authorizationServerId);
        return Uni.createFrom().item(AuthorizationServerResponse.newBuilder().setId(request.getId()).build());
    }

    @Override
    @Blocking
    public Uni<AuthorizationServerResponse> updateAuthorizationServer(AuthorizationServerUpdateRequest request) {
        try{
            var authorizationServer = authorizationServerService.getAuthorizationServer(UUID.fromString(request.getId()));
            authorizationServer.setName(request.getName());
            authorizationServer.setAudience(request.getAudience());
            authorizationServer.setServerUrl(URI.create(request.getServerUrl()).toURL());
            authorizationServer.setAuthorizationCodeTokenExpiration(request.getAuthorizationCodeTokenExpiration());
            authorizationServer.setClientCredentialsTokenExpiration(request.getClientCredentialsTokenExpiration());
            authorizationServer.setMetadata(MetadataMapper.toMetadata(request.getMetadata()));
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
                                    .setMetadata(MetadataMapper.from(authorizationServer.getMetadata()))
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
    public Uni<AuthorizationServerResponse> getAuthorizationServer(AuthorizationServerGetRequest request) {
        try {
            var authorizationServer = authorizationServerService.getAuthorizationServer(UUID.fromString(request.getId()));
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
                                    .setMetadata(MetadataMapper.from(authorizationServer.getMetadata()))
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(authorizationServer.getUpdatedOn().toEpochSecond()).build())
                                    .addAllScopes(
                                            authorizationServer
                                                    .getScopes()
                                                    .stream()
                                                    .map(scope -> com.cartobucket.auth.data.rpc.Scope
                                                            .newBuilder()
                                                            .setId(String.valueOf(scope.getId()))
                                                            .setName(scope.getName())
                                                            .setAuthorizationServerId(String.valueOf(scope.getAuthorizationServer().getId()))
                                                            .setMetadata(MetadataMapper.from(scope.getMetadata()))
                                                            .setCreatedOn(Timestamp.newBuilder().setSeconds(scope.getCreatedOn().toEpochSecond()).build())
                                                            .setUpdatedOn(Timestamp.newBuilder().setSeconds(scope.getUpdatedOn().toEpochSecond()).build())
                                                            .build()
                                                    )
                                                    .toList()
                                    )
                                    .build()
                    );
        }
        catch (AuthorizationServerNotFound e) {
            throw Status.fromCode(Status.Code.NOT_FOUND).withDescription(e.getMessage()).withCause(e).asRuntimeException();
        }
    }

    @Override
    @Blocking
    public Uni<AuthorizationServerSigningKeyResponse> getAuthorizationServerSigningKey(AuthorizationServerGetRequest request) {
        final var signingKey = authorizationServerService.getSigningKeysForAuthorizationServer(UUID.fromString(request.getId()));
        return Uni
                .createFrom()
                .item(
                        AuthorizationServerSigningKeyResponse
                                .newBuilder()
                                .setId(String.valueOf(signingKey.getId()))
                                .setAlgorithm(signingKey.getKeyType())
                                .setPrivateKey(signingKey.getPrivateKey())
                                .setPublicKey(signingKey.getPublicKey())
                                .setAuthorizationServerId(String.valueOf(signingKey.getAuthorizationServerId()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(signingKey.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(signingKey.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<JwksResponse> getAuthorizationServerJwks(AuthorizationServerGetRequest request) {
        final var jwks = authorizationServerService.getJwksForAuthorizationServer(UUID.fromString(request.getId()));
        return Uni
                .createFrom()
                .item(
                        JwksResponse
                                .newBuilder()
                                .addAllJwks(
                                        jwks
                                                .stream()
                                                .map(jwk ->
                                                        Jwk
                                                                .newBuilder()
                                                                .setAlg(jwk.getAlg())
                                                                .setKid(jwk.getKid())
                                                                .setKty(jwk.getKty())
                                                                .setE(jwk.getE())
                                                                .setN(jwk.getN())
                                                                .setUse(jwk.getUse())
                                                                .setX5C(String.valueOf(jwk.getX5c()))
                                                                .setX5T(jwk.getX5t())
                                                                .setX5TS256Bytes(ByteString.fromHex(jwk.getX5tHashS256()))
                                                                .build()
                                                )
                                                .toList()
                                )
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ValidateJwtForAuthorizationServerResponse> validateJwtForAuthorizationServer(ValidateJwtForAuthorizationServerRequest request) {
        final var claims = authorizationServerService
                .validateJwtForAuthorizationServer(
                        UUID.fromString(request.getAuthorizationServerId()),
                        request.getJwt()
                );

        return Uni
                .createFrom()
                .item(ValidateJwtForAuthorizationServerResponse
                        .newBuilder()
                        .setClaims(ProfileMapper.toProtoMap(claims))
                        .build()
                );
    }

    @Override
    @Blocking
    public Uni<GenerateAccessTokenResponse> generateAccessToken(GenerateAccessTokenRequest request) {
        final var accessToken = authorizationServerService.generateAccessToken(
                UUID.fromString(request.getAuthorizationServerId()),
                UUID.fromString(request.getUserId()),
                request.getSubject(),
                request.getScopesList().stream().map(ScopeMapper::fromResponse).toList(),
                request.getExpireInSeconds(),
                request.getNonce()
        );

        return Uni
                .createFrom()
                .item(
                        GenerateAccessTokenResponse
                                .newBuilder()
                                .setAccessToken(accessToken.getAccessToken())
                                .setIdToken(accessToken.getIdToken())
                                .setExpireInSeconds(accessToken.getExpiresIn())
                                .setScope(accessToken.getScope())
                                .setRefreshToken(accessToken.getRefreshToken() != null ? accessToken.getAccessToken() : "")
                                .build()
                );
    }
}
