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

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.model.generated.ScopeRequest;
import com.cartobucket.auth.model.generated.ScopeResponse;
import com.cartobucket.auth.data.domain.Scope;

public class ScopeMapper {
    public static Scope to(ScopeRequest scopeRequest) {
        var scope = new Scope();
        scope.setName(scopeRequest.getName());
        final var authorizationServer = new AuthorizationServer();
        authorizationServer.setId(scopeRequest.getAuthorizationServerId());
        scope.setAuthorizationServer(authorizationServer);
        scope.setMetadata(MetadataMapper.from(scopeRequest.getMetadata()));
        return scope;
    }

    public static ScopeResponse toResponse(Scope scope) {
        var response = new ScopeResponse();
        response.setId(String.valueOf(scope.getId()));
        response.setAuthorizationServerId(scope.getAuthorizationServer().getId());
        response.setName(scope.getName());
        response.setMetadata(MetadataMapper.to(scope.getMetadata()));
        response.setCreatedOn(scope.getCreatedOn());
        response.setUpdatedOn(scope.getUpdatedOn());
        return response;
    }

    public static ScopeResponse toSummaryResponse(Scope scope) {
        var response = new ScopeResponse();
        response.setId(String.valueOf(scope.getId()));
        response.setName(scope.getName());
        return response;
    }
}
