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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.JWK;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.services.impls.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.rpc.AuthorizationServerCreateRequest;
import com.cartobucket.auth.rpc.AuthorizationServerDeleteRequest;
import com.cartobucket.auth.rpc.AuthorizationServerGetRequest;
import com.cartobucket.auth.rpc.AuthorizationServerListRequest;
import com.cartobucket.auth.rpc.AuthorizationServerUpdateRequest;
import com.cartobucket.auth.rpc.Jwk;
import com.cartobucket.auth.rpc.MutinyAuthorizationServersGrpc;
import com.cartobucket.auth.rpc.ValidateJwtForAuthorizationServerRequest;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class AuthorizationServerService implements com.cartobucket.auth.data.services.AuthorizationServerService {
    @Inject
    @GrpcClient("authorizationServers")
    MutinyAuthorizationServersGrpc.MutinyAuthorizationServersStub authorizationServerClient;

    @Override
    public AuthorizationServer getAuthorizationServer(UUID authorizationServerId) {
        var authorizationServer = AuthorizationServerMapper.toAuthorizationServer(
                authorizationServerClient
                        .getAuthorizationServer(
                                AuthorizationServerGetRequest
                                        .newBuilder()
                                        .setId(String.valueOf(authorizationServerId))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
                );
        return authorizationServer;
    }

    @Override
    public AuthorizationServer createAuthorizationServer(AuthorizationServer authorizationServer) {
        return AuthorizationServerMapper.toAuthorizationServer(
                authorizationServerClient.createAuthorizationServer(
                        AuthorizationServerCreateRequest
                                .newBuilder()
                                .setName(authorizationServer.getName())
                                .setAudience(authorizationServer.getAudience())
                                .setServerUrl(String.valueOf(authorizationServer.getServerUrl()))
                                .setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration())
                                .setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration())
                                .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public AuthorizationServer updateAuthorizationServer(UUID authorizationServerId, AuthorizationServer authorizationServer) throws AuthorizationServerNotFound {
        return AuthorizationServerMapper.toAuthorizationServer(
                authorizationServerClient.updateAuthorizationServer(
                                AuthorizationServerUpdateRequest.newBuilder()
                                        .setId(String.valueOf(authorizationServerId))
                                        .setName(authorizationServer.getName())
                                        .setAudience(authorizationServer.getAudience())
                                        .setServerUrl(String.valueOf(authorizationServer.getServerUrl()))
                                        .setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration())
                                        .setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration())
                                        .build()

                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public List<AuthorizationServer> getAuthorizationServers() {
        return authorizationServerClient.listAuthorizationServers(
                AuthorizationServerListRequest
                        .newBuilder()
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getAuthorizationServersList()
                .stream()
                .map(AuthorizationServerMapper::toAuthorizationServer)
                .toList();
    }

    @Override
    public void deleteAuthorizationServer(UUID authorizationServerId) throws AuthorizationServerNotFound {
        authorizationServerClient.deleteAuthorizationServer(
                        AuthorizationServerDeleteRequest
                                .newBuilder()
                                .setId(String.valueOf(authorizationServerId))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public SigningKey getSigningKeysForAuthorizationServer(final UUID authorizationServerId) {
        final var signingKey = authorizationServerClient
                .getAuthorizationServerSigningKey(
                        AuthorizationServerGetRequest
                                .newBuilder()
                                .setId(String.valueOf(authorizationServerId))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
        return AuthorizationServerMapper.toSigningKey(signingKey);
    }


    @Override
    public List<JWK> getJwksForAuthorizationServer(UUID authorizationServerId) {
        final var jwks = authorizationServerClient
                .getAuthorizationServerJwks(
                        AuthorizationServerGetRequest
                                .newBuilder()
                                .setId(String.valueOf(authorizationServerId))
                                .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
        return jwks.getJwksList().stream().map(
                jwk -> {
                    final var _jwk = new JWK();
                    _jwk.setAlg(jwk.getAlg());
                    _jwk.setKid(jwk.getKid());
                    _jwk.setKty(jwk.getKty());
                    _jwk.setUse(jwk.getUse());
                    _jwk.setN(jwk.getN());
                    _jwk.setE(jwk.getE());
                    return _jwk;
                }
        ).toList();
    }

    @Override
    public Map<String, Object> validateJwtForAuthorizationServer(UUID authorizationServerId, String Jwt) throws NotAuthorized {
        final var claims = authorizationServerClient.validateJwtForAuthorizationServer(
                ValidateJwtForAuthorizationServerRequest
                        .newBuilder()
                        .setAuthorizationServerId(String.valueOf(authorizationServerId))
                        .setJwt(Jwt)
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
        return Profile.fromProtoMap(claims.getClaims().getFieldsMap());
    }
}
