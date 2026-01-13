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

package com.revethq.auth.web.api.routes.mappers

import com.revethq.auth.core.domain.AuthorizationServer
import com.revethq.auth.core.api.dto.ScopeRequest
import com.revethq.auth.core.api.dto.ScopeResponse
import com.revethq.auth.core.domain.Scope

object ScopeMapper {

    @JvmStatic
    fun to(scopeRequest: ScopeRequest): Scope {
        val scope = Scope()
        scope.name = scopeRequest.name
        val authorizationServer = AuthorizationServer()
        authorizationServer.id = scopeRequest.authorizationServerId
        scope.authorizationServer = authorizationServer
        scope.metadata = MetadataMapper.from(scopeRequest.metadata)
        return scope
    }

    @JvmStatic
    fun toResponse(scope: Scope): ScopeResponse {
        return ScopeResponse().apply {
            id = scope.id.toString()
            authorizationServerId = scope.authorizationServer?.id
            name = scope.name
            metadata = MetadataMapper.to(scope.metadata)
            createdOn = scope.createdOn
            updatedOn = scope.updatedOn
        }
    }

    @JvmStatic
    fun toSummaryResponse(scope: Scope): ScopeResponse {
        return ScopeResponse().apply {
            id = scope.id.toString()
            name = scope.name
        }
    }
}
