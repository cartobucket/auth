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

package com.cartobucket.auth.postgres.client.entities.mappers

import com.cartobucket.auth.data.domain.AuthorizationServer

object AuthorizationServerMapper {
    fun from(authorizationServer: com.cartobucket.auth.postgres.client.entities.AuthorizationServer): AuthorizationServer {
        val domainAuthorizationServer = AuthorizationServer()
        domainAuthorizationServer.id = authorizationServer.id
        domainAuthorizationServer.serverUrl = authorizationServer.serverUrl
        domainAuthorizationServer.name = authorizationServer.name
        domainAuthorizationServer.audience = authorizationServer.audience
        domainAuthorizationServer.clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration
        domainAuthorizationServer.authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration
        domainAuthorizationServer.metadata = authorizationServer.metadata
        domainAuthorizationServer.scopes = authorizationServer.scopes?.map { ScopeMapper.fromNoAuthorizationServer(it) } ?: emptyList()
        domainAuthorizationServer.createdOn = authorizationServer.createdOn
        domainAuthorizationServer.updatedOn = authorizationServer.updatedOn
        return domainAuthorizationServer
    }

    fun to(authorizationServer: AuthorizationServer): com.cartobucket.auth.postgres.client.entities.AuthorizationServer {
        val entityAuthorizationServer =
            com.cartobucket.auth.postgres.client.entities
                .AuthorizationServer()
        entityAuthorizationServer.id = authorizationServer.id
        entityAuthorizationServer.serverUrl = authorizationServer.serverUrl
        entityAuthorizationServer.name = authorizationServer.name
        entityAuthorizationServer.audience = authorizationServer.audience
        entityAuthorizationServer.clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration
        entityAuthorizationServer.authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration
        entityAuthorizationServer.metadata = authorizationServer.metadata
        entityAuthorizationServer.createdOn = authorizationServer.createdOn
        entityAuthorizationServer.updatedOn = authorizationServer.updatedOn
        return entityAuthorizationServer
    }
}
