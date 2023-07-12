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

import com.cartobucket.auth.data.services.impls.mappers.ScopeMapper;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound;
import com.cartobucket.auth.data.rpc.MutinyScopesGrpc;
import com.cartobucket.auth.data.rpc.ScopeListRequest;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class ScopeService implements com.cartobucket.auth.data.services.ScopeService {

    @Inject
    @GrpcClient("scopes") MutinyScopesGrpc.MutinyScopesStub scopesClient;

//    public ScopeService(Scopes scopes) {
//        this.scopes = scopes;
//    }

    @Override
    public List<Scope> getScopes(List<UUID> authorizationServerIds) {
        var scopesRequest = ScopeListRequest.newBuilder().build();
        final var scopes = scopesClient.listScopes(scopesRequest);
        return scopes
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getScopesList()
                .stream()
                .map(ScopeMapper::toScope)
                .toList();
    }

    @Override
    public Scope createScope(Scope scope) {
        return null;
    }

    @Override
    public void deleteScope(UUID scopeId) throws ScopeNotFound {

    }

    @Override
    public Scope getScope(UUID scopeId) throws ScopeNotFound {
        return null;
    }

    @Override
    public List<String> filterScopesForAuthorizationServerId(UUID authorizationServerId, String scopes) {
        return null;
    }
}
