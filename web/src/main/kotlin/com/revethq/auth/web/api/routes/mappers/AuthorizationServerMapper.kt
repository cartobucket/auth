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

import com.revethq.auth.core.api.dto.AuthorizationServerRequest
import com.revethq.auth.core.api.dto.AuthorizationServerResponse
import com.revethq.auth.core.domain.AuthorizationServer
import java.net.URI

object AuthorizationServerMapper {

    @JvmStatic
    fun from(request: AuthorizationServerRequest): AuthorizationServer {
        val authorizationServer = AuthorizationServer()
        authorizationServer.serverUrl = URI.create(request.serverUrl).toURL()
        authorizationServer.name = request.name
        authorizationServer.audience = request.audience
        authorizationServer.authorizationCodeTokenExpiration = request.authorizationCodeTokenExpiration?.toLong()
        authorizationServer.clientCredentialsTokenExpiration = request.clientCredentialsTokenExpiration?.toLong()
        authorizationServer.metadata = MetadataMapper.from(request.metadata)
        return authorizationServer
    }

    @JvmStatic
    fun toResponse(authorizationServer: AuthorizationServer): AuthorizationServerResponse {
        return AuthorizationServerResponse().apply {
            id = authorizationServer.id.toString()
            name = authorizationServer.name
            serverUrl = authorizationServer.serverUrl.toString()
            audience = authorizationServer.audience
            authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration?.toInt()
            clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration?.toInt()
            metadata = MetadataMapper.to(authorizationServer.metadata)
            scopes = authorizationServer.scopes?.map { ScopeMapper.toSummaryResponse(it) } ?: emptyList()
        }
    }
}
