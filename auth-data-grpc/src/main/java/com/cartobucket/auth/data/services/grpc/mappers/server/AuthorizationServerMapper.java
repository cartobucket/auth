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

package com.cartobucket.auth.data.services.grpc.mappers.server;

import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.rpc.AuthorizationServerCreateResponse;
import com.cartobucket.auth.rpc.AuthorizationServerResponse;
import com.cartobucket.auth.rpc.AuthorizationServerSigningKeyResponse;
import com.cartobucket.auth.rpc.GenerateAccessTokenResponse;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class AuthorizationServerMapper {
    public static AuthorizationServer toAuthorizationServer(AuthorizationServerResponse authorizationServerResponse) {
        var authorizationServer = new AuthorizationServer();
        authorizationServer.setId(UUID.fromString(authorizationServerResponse.getId()));
        try {
            authorizationServer.setServerUrl(URI.create(authorizationServerResponse.getServerUrl()).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        authorizationServer.setAudience(authorizationServerResponse.getAudience());
        authorizationServer.setName(authorizationServerResponse.getName());
        authorizationServer.setAuthorizationCodeTokenExpiration(authorizationServerResponse.getAuthorizationCodeTokenExpiration());
        authorizationServer.setClientCredentialsTokenExpiration(authorizationServerResponse .getClientCredentialsTokenExpiration());
        authorizationServer.setMetadata(MetadataMapper.from(authorizationServerResponse.getMetadata()));
        authorizationServer.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(authorizationServerResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        authorizationServer.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(authorizationServerResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        authorizationServer.setScopes(authorizationServerResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        return authorizationServer;
    }

    public static AuthorizationServer toAuthorizationServer(AuthorizationServerCreateResponse authorizationServerCreateResponse) {
        var authorizationServer = new AuthorizationServer();
        authorizationServer.setId(UUID.fromString(authorizationServerCreateResponse.getId()));
        try {
            authorizationServer.setServerUrl(URI.create(authorizationServerCreateResponse.getServerUrl()).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        authorizationServer.setAudience(authorizationServerCreateResponse.getAudience());
        authorizationServer.setName(authorizationServerCreateResponse.getName());
        authorizationServer.setAuthorizationCodeTokenExpiration(authorizationServerCreateResponse.getAuthorizationCodeTokenExpiration());
        authorizationServer.setClientCredentialsTokenExpiration(authorizationServerCreateResponse.getClientCredentialsTokenExpiration());
        authorizationServer.setMetadata(MetadataMapper.from(authorizationServerCreateResponse.getMetadata()));
        authorizationServer.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(authorizationServerCreateResponse.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        authorizationServer.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(authorizationServerCreateResponse.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        authorizationServer.setScopes(authorizationServerCreateResponse.getScopesList().stream().map(ScopeMapper::toScope).toList());
        return authorizationServer;
    }

    public static SigningKey toSigningKey(AuthorizationServerSigningKeyResponse signingKey) {
        var _signingKey = new SigningKey();
        _signingKey.setId(UUID.fromString(signingKey.getId()));
        _signingKey.setAuthorizationServerId(UUID.fromString(signingKey.getAuthorizationServerId()));
        _signingKey.setKeyType(signingKey.getAlgorithm());
        _signingKey.setPrivateKey(signingKey.getPrivateKey());
        _signingKey.setPublicKey(signingKey.getPublicKey());
        _signingKey.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(signingKey.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
        _signingKey.setUpdatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(signingKey.getUpdatedOn().getSeconds()), ZoneId.of("UTC")));
        return _signingKey;
    }

    public static AccessToken toAccessToken(GenerateAccessTokenResponse generateAccessTokenResponse) {
        var accessToken = new AccessToken();
        accessToken.setAccessToken(generateAccessTokenResponse.getAccessToken());
        accessToken.setExpiresIn(Math.toIntExact(generateAccessTokenResponse.getExpireInSeconds()));
        accessToken.setTokenType(AccessToken.TokenTypeEnum.BEARER);
        accessToken.setScope(generateAccessTokenResponse.getScope());
        accessToken.setRefreshToken(generateAccessTokenResponse.getRefreshToken());
        accessToken.setIdToken(generateAccessTokenResponse.getIdToken());
        return accessToken;
    }
}
