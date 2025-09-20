// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.AuthorizationServerRequest
import com.cartobucket.auth.api.dto.AuthorizationServersResponse
import com.cartobucket.auth.api.interfaces.AuthorizationServersApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.AuthorizationServerMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.AuthorizationServerService
import jakarta.ws.rs.core.Response
import java.util.UUID

class AuthorizationServers(
    private val authorizationServerService: AuthorizationServerService,
) : AuthorizationServersApi {
    override fun createAuthorizationServer(authorizationServerRequest: AuthorizationServerRequest): Response =
        Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.createAuthorizationServer(
                        AuthorizationServerMapper.from(authorizationServerRequest),
                    ),
                ),
            ).build()

    override fun deleteAuthorizationServer(authorizationServerId: UUID): Response {
        authorizationServerService.deleteAuthorizationServer(authorizationServerId)
        return Response.noContent().build()
    }

    override fun getAuthorizationServer(authorizationServerId: UUID): Response =
        Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.getAuthorizationServer(authorizationServerId),
                ),
            ).build()

    override fun listAuthorizationServers(
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT

        val servers =
            authorizationServerService
                .getAuthorizationServers(Page(actualLimit, actualOffset))
                .map { AuthorizationServerMapper.toResponse(it) }
        val page = getPage("authorizationServers", emptyList(), actualLimit, actualOffset)
        val authorizationServersResponse =
            AuthorizationServersResponse(
                authorizationServers = servers,
                page = page,
            )
        return Response
            .ok()
            .entity(authorizationServersResponse)
            .build()
    }

    override fun updateAuthorizationServer(
        authorizationServerId: UUID,
        authorizationServerRequest: AuthorizationServerRequest,
    ): Response =
        Response
            .ok()
            .entity(
                AuthorizationServerMapper.toResponse(
                    authorizationServerService.updateAuthorizationServer(
                        authorizationServerId,
                        AuthorizationServerMapper.from(authorizationServerRequest),
                    ),
                ),
            ).build()
}
