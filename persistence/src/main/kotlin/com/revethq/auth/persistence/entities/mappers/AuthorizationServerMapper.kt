/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.persistence.entities.mappers

import com.revethq.auth.core.domain.AuthorizationServer
import com.revethq.auth.persistence.entities.AuthorizationServer as PostgresAuthorizationServer

object AuthorizationServerMapper {
    @JvmStatic
    fun from(authorizationServer: PostgresAuthorizationServer): AuthorizationServer {
        return AuthorizationServer(
            id = authorizationServer.id,
            serverUrl = authorizationServer.serverUrl,
            audience = authorizationServer.audience,
            name = authorizationServer.name,
            authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration,
            clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration,
            metadata = authorizationServer.metadata,
            createdOn = authorizationServer.createdOn,
            updatedOn = authorizationServer.updatedOn,
            scopes = authorizationServer.scopes?.map { ScopeMapper.fromNoAuthorizationServer(it) } ?: emptyList()
        )
    }

    @JvmStatic
    fun to(authorizationServer: AuthorizationServer): PostgresAuthorizationServer {
        return PostgresAuthorizationServer().apply {
            id = authorizationServer.id
            serverUrl = authorizationServer.serverUrl
            audience = authorizationServer.audience
            name = authorizationServer.name
            authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration
            clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration
            metadata = authorizationServer.metadata
            createdOn = authorizationServer.createdOn
            updatedOn = authorizationServer.updatedOn
            scopes = authorizationServer.scopes?.map { ScopeMapper.to(it) } ?: emptyList()
        }
    }
}
