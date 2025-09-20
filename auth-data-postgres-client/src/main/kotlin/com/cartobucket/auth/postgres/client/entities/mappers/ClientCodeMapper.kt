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

import com.cartobucket.auth.data.domain.ClientCode
import com.cartobucket.auth.data.services.ScopeService

object ClientCodeMapper {
    fun from(
        clientCode: com.cartobucket.auth.postgres.client.entities.ClientCode,
        scopeService: ScopeService,
    ): ClientCode {
        val domainClientCode = ClientCode()
        domainClientCode.code = clientCode.code
        domainClientCode.clientId = clientCode.clientId.toString()
        domainClientCode.codeChallenge = clientCode.codeChallenge
        domainClientCode.codeChallengeMethod = clientCode.codeChallengeMethod
        domainClientCode.id = clientCode.id
        domainClientCode.nonce = clientCode.nonce
        domainClientCode.state = clientCode.state
        domainClientCode.authorizationServerId = clientCode.authorizationServerId
        domainClientCode.redirectUri = clientCode.redirectUri
        domainClientCode.userId = clientCode.userId
        domainClientCode.createdOn = clientCode.createdOn

        // Get scopes using the service
        domainClientCode.scopes = clientCode.scopeIds?.let { scopeIds ->
            scopeService.getScopes(listOf(clientCode.authorizationServerId!!), scopeIds)
        } ?: emptyList()

        return domainClientCode
    }

    fun to(clientCode: ClientCode): com.cartobucket.auth.postgres.client.entities.ClientCode {
        val entityClientCode =
            com.cartobucket.auth.postgres.client.entities
                .ClientCode()
        entityClientCode.code = clientCode.code
        entityClientCode.clientId = clientCode.clientId?.let { java.util.UUID.fromString(it) }
        entityClientCode.codeChallenge = clientCode.codeChallenge
        entityClientCode.codeChallengeMethod = clientCode.codeChallengeMethod
        entityClientCode.id = clientCode.id
        entityClientCode.nonce = clientCode.nonce
        entityClientCode.state = clientCode.state
        entityClientCode.authorizationServerId = clientCode.authorizationServerId
        entityClientCode.redirectUri = clientCode.redirectUri
        entityClientCode.userId = clientCode.userId
        entityClientCode.createdOn = clientCode.createdOn
        entityClientCode.scopeIds = clientCode.scopes?.map { it.id!! }
        return entityClientCode
    }
}
