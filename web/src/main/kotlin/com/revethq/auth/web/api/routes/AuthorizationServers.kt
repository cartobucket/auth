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
package com.revethq.auth.web.api.routes

import com.revethq.auth.core.api.dto.AuthorizationServerRequest
import com.revethq.auth.core.api.dto.AuthorizationServersResponse
import com.revethq.auth.core.api.interfaces.AuthorizationServersApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.Pagination.getPage
import com.revethq.auth.web.api.routes.mappers.AuthorizationServerMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.AuthorizationServerService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class AuthorizationServers @Inject constructor(
    val authorizationServerService: AuthorizationServerService
) : AuthorizationServersApi {

    override fun createAuthorizationServer(authorizationServerRequest: AuthorizationServerRequest): Response {
        return Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.createAuthorizationServer(
                        AuthorizationServerMapper.from(authorizationServerRequest)
                    )
                )
            )
            .build()
    }

    override fun deleteAuthorizationServer(authorizationServerId: UUID): Response {
        authorizationServerService.deleteAuthorizationServer(authorizationServerId)
        return Response.noContent().build()
    }

    override fun getAuthorizationServer(authorizationServerId: UUID): Response {
        return Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.getAuthorizationServer(authorizationServerId)
                )
            )
            .build()
    }

    override fun listAuthorizationServers(limit: Int?, offset: Int?): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT

        val authorizationServersResponse = AuthorizationServersResponse()
        authorizationServersResponse.authorizationServers = authorizationServerService
            .getAuthorizationServers(Page(actualLimit, actualOffset))
            .map { AuthorizationServerMapper.toResponse(it) }
        authorizationServersResponse.page = getPage("authorizationServers", emptyList(), actualLimit, actualOffset)
        return Response.ok().entity(authorizationServersResponse).build()
    }

    override fun updateAuthorizationServer(
        authorizationServerId: UUID,
        authorizationServerRequest: AuthorizationServerRequest
    ): Response {
        return Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.updateAuthorizationServer(
                        authorizationServerId,
                        AuthorizationServerMapper.from(authorizationServerRequest)
                    )
                )
            )
            .build()
    }
}
