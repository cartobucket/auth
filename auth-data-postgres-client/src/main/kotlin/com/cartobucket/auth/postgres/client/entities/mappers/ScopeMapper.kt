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
import com.cartobucket.auth.data.domain.Scope

object ScopeMapper {
    fun from(scope: com.cartobucket.auth.postgres.client.entities.Scope): Scope {
        val domainScope = Scope()
        domainScope.id = scope.id
        domainScope.createdOn = scope.createdOn
        domainScope.updatedOn = scope.updatedOn
        domainScope.name = scope.name
        domainScope.authorizationServer = scope.authorizationServer?.let { AuthorizationServerMapper.from(it) }
        domainScope.metadata = scope.metadata
        return domainScope
    }

    fun fromNoAuthorizationServer(scope: com.cartobucket.auth.postgres.client.entities.Scope): Scope {
        val domainScope = Scope()
        domainScope.id = scope.id
        domainScope.createdOn = scope.createdOn
        domainScope.updatedOn = scope.updatedOn
        domainScope.name = scope.name
        val authorizationServer = AuthorizationServer()
        authorizationServer.id = scope.authorizationServer?.id
        domainScope.authorizationServer = authorizationServer
        domainScope.metadata = scope.metadata
        return domainScope
    }

    fun to(scope: Scope): com.cartobucket.auth.postgres.client.entities.Scope {
        val entityScope =
            com.cartobucket.auth.postgres.client.entities
                .Scope()
        entityScope.id = scope.id
        entityScope.createdOn = scope.createdOn
        entityScope.updatedOn = scope.updatedOn
        entityScope.name = scope.name
        entityScope.authorizationServer = scope.authorizationServer?.let { AuthorizationServerMapper.to(it) }
        entityScope.metadata = scope.metadata
        return entityScope
    }
}
