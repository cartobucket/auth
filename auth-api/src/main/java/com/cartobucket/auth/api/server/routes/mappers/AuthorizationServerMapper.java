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

import com.cartobucket.auth.model.generated.AuthorizationServerRequest;
import com.cartobucket.auth.model.generated.AuthorizationServerResponse;
import com.cartobucket.auth.data.domain.AuthorizationServer;

import java.net.MalformedURLException;
import java.net.URI;

public class AuthorizationServerMapper {
    public static AuthorizationServer from(AuthorizationServerRequest request) {
        var authorizationServer = new AuthorizationServer();
        try {
            authorizationServer.setServerUrl(URI.create(request.getServerUrl()).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        authorizationServer.setName(request.getName());
        authorizationServer.setAudience(request.getAudience());
        authorizationServer.setAuthorizationCodeTokenExpiration(Long.valueOf(request.getAuthorizationCodeTokenExpiration()));
        authorizationServer.setClientCredentialsTokenExpiration(Long.valueOf(request.getClientCredentialsTokenExpiration()));
        authorizationServer.setMetadata(MetadataMapper.from(request.getMetadata()));
        return authorizationServer;
    }

    public static AuthorizationServerResponse toResponse(AuthorizationServer authorizationServer) {
        var response = new AuthorizationServerResponse();
        response.setId(String.valueOf(authorizationServer.getId()));
        response.setName(authorizationServer.getName());
        response.setServerUrl(String.valueOf(authorizationServer.getServerUrl()));
        response.setAudience(authorizationServer.getAudience());
        response.setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration().intValue());
        response.setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration().intValue());
        response.setMetadata(MetadataMapper.to(authorizationServer.getMetadata()));
        response.setScopes(authorizationServer.getScopes().stream().map(ScopeMapper::toSummaryResponse).toList());
        return response;
    }
}
