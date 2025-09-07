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

package com.cartobucket.auth.data.services

import com.cartobucket.auth.data.domain.AccessToken
import com.cartobucket.auth.data.domain.AuthorizationServer
import com.cartobucket.auth.data.domain.JWK
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.domain.SigningKey
import com.cartobucket.auth.data.exceptions.NotAuthorized
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound
import java.util.UUID

interface AuthorizationServerService {
    // This method returns the actual AuthorizationServer object as it is a dependency of the rest of the system.
    @Throws(AuthorizationServerNotFound::class)
    fun getAuthorizationServer(authorizationServerId: UUID): AuthorizationServer

    fun createAuthorizationServer(authorizationServer: AuthorizationServer): AuthorizationServer

    @Throws(AuthorizationServerNotFound::class)
    fun updateAuthorizationServer(authorizationServerId: UUID, authorizationServer: AuthorizationServer): AuthorizationServer

    fun getAuthorizationServers(page: Page): List<AuthorizationServer>

    @Throws(AuthorizationServerNotFound::class)
    fun deleteAuthorizationServer(authorizationServerId: UUID)

    fun getSigningKeysForAuthorizationServer(authorizationServerId: UUID): SigningKey

    @Throws(NotAuthorized::class)
    fun validateJwtForAuthorizationServer(authorizationServerId: UUID, jwt: String): Map<String, Any>

    fun getJwksForAuthorizationServer(authorizationServerId: UUID): List<JWK>

    fun generateAccessToken(
        authorizationServerId: UUID,
        userId: UUID,
        subject: String,
        scopes: List<Scope>,
        expireInSeconds: Long,
        nonce: String
    ): AccessToken
    
    fun generateAccessTokenWithClientId(
        authorizationServerId: UUID,
        userId: UUID,
        subject: String,
        clientId: String,
        scopes: List<Scope>,
        expireInSeconds: Long,
        nonce: String
    ): AccessToken
    
    fun refreshAccessToken(
        authorizationServerId: UUID,
        refreshToken: String,
        clientId: String
    ): AccessToken
}