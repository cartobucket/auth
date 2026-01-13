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

import com.revethq.auth.core.domain.ClientCode
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.persistence.entities.ClientCode as PostgresClientCode
import java.util.UUID

object ClientCodeMapper {
    @JvmStatic
    fun from(clientCode: PostgresClientCode, scopeService: ScopeService): ClientCode {
        val scopes = if (!clientCode.scopeIds.isNullOrEmpty()) {
            clientCode.scopeIds!!
                .filterNotNull()
                .mapNotNull { scopeId ->
                    try {
                        scopeService.getScope(scopeId)
                    } catch (e: Exception) {
                        System.err.println("Failed to load scope with ID: $scopeId - ${e.message}")
                        null
                    }
                }
        } else {
            emptyList()
        }

        return ClientCode(
            code = clientCode.code,
            clientId = clientCode.clientId?.toString(),
            codeChallenge = clientCode.codeChallenge,
            codeChallengeMethod = clientCode.codeChallengeMethod,
            id = clientCode.id,
            nonce = clientCode.nonce,
            state = clientCode.state,
            authorizationServerId = clientCode.authorizationServerId,
            redirectUri = clientCode.redirectUri,
            userId = clientCode.userId,
            scopes = scopes,
            createdOn = clientCode.createdOn
        )
    }

    @JvmStatic
    fun to(clientCode: ClientCode): PostgresClientCode {
        return PostgresClientCode().apply {
            code = clientCode.code
            this.clientId = clientCode.clientId?.let { UUID.fromString(it) }
            codeChallenge = clientCode.codeChallenge
            codeChallengeMethod = clientCode.codeChallengeMethod
            id = clientCode.id
            nonce = clientCode.nonce
            state = clientCode.state
            authorizationServerId = clientCode.authorizationServerId
            redirectUri = clientCode.redirectUri
            userId = clientCode.userId
            scopeIds = clientCode.scopes?.mapNotNull { it.id } ?: emptyList()
            createdOn = clientCode.createdOn
        }
    }
}
