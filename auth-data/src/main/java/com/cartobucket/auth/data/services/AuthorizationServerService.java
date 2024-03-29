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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.JWK;
import com.cartobucket.auth.data.domain.SigningKey;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface AuthorizationServerService {
    // This method returns the actual AuthorizationServer object as it is a dependency of the rest of the system.
    AuthorizationServer getAuthorizationServer(final UUID authorizationServerId) throws AuthorizationServerNotFound;
    AuthorizationServer createAuthorizationServer(final AuthorizationServer authorizationServer);
    AuthorizationServer updateAuthorizationServer(final UUID authorizationServerId, final AuthorizationServer authorizationServer) throws AuthorizationServerNotFound;
    List<AuthorizationServer> getAuthorizationServers(Page page);
    void deleteAuthorizationServer(final UUID authorizationServerId) throws AuthorizationServerNotFound;
    SigningKey getSigningKeysForAuthorizationServer(final UUID authorizationServerId);
    Map<String, Object> validateJwtForAuthorizationServer(final UUID authorizationServerId, final String Jwt) throws NotAuthorized;
    List<JWK> getJwksForAuthorizationServer(final UUID authorizationServerId);
    AccessToken generateAccessToken(final UUID authorizationServerId, final UUID userId, final String subject, final List<Scope> scopes, final long expireInSeconds, final String nonce);
}
