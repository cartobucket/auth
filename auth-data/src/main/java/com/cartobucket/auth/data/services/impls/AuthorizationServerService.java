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
import com.cartobucket.auth.data.domain.JWKS;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.rpc.MutinyScopesGrpc;
import com.cartobucket.auth.data.services.impls.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.rpc.AuthorizationServerGetRequest;
import com.cartobucket.auth.rpc.MutinyAuthorizationServersGrpc;
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
    public JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer) {
        return null;
    }

    @Override
    public JWKS getJwksForAuthorizationServer(UUID authorizationServerId) {
        return null;
    }

    @Override
    public AuthorizationServer createAuthorizationServer(AuthorizationServer authorizationServer) {
        return null;
    }

    @Override
    public AuthorizationServer updateAuthorizationServer(UUID authorizationServerId, AuthorizationServer authorizationServer) throws AuthorizationServerNotFound {
        return null;
    }

    @Override
    public List<AuthorizationServer> getAuthorizationServers() {
        return null;
    }

    @Override
    public void deleteAuthorizationServer(UUID authorizationServerId) throws AuthorizationServerNotFound {

    }

    @Override
    public SigningKey getSigningKeysForAuthorizationServer(AuthorizationServer authorizationServer) {
        return null;
    }

    @Override
    public Map<String, Object> validateJwtForAuthorizationServer(AuthorizationServer authorizationServer, String Jwt) throws NotAuthorized {
        return null;
    }
}
