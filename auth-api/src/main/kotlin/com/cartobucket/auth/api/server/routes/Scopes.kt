// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.ScopeRequest
import com.cartobucket.auth.api.dto.ScopesResponse
import com.cartobucket.auth.api.interfaces.ScopesApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.ScopeMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.ScopeService
import jakarta.ws.rs.core.Response
import java.util.UUID

class Scopes(
    private val scopeService: ScopeService,
) : ScopesApi {
    override fun createScope(scopeRequest: ScopeRequest): Response =
        Response
            .ok()
            .entity(
                ScopeMapper.toResponse(
                    scopeService.createScope(ScopeMapper.to(scopeRequest)),
                ),
            ).build()

    override fun deleteScope(scopeId: UUID): Response {
        scopeService.deleteScope(scopeId)
        return Response.noContent().build()
    }

    override fun getScope(scopeId: UUID): Response =
        Response
            .ok()
            .entity(
                ScopeMapper.toResponse(scopeService.getScope(scopeId)),
            ).build()

    override fun listScopes(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val scopes =
            scopeService
                .getScopes(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { ScopeMapper.toResponse(it) }
        val page = getPage("scopes", actualAuthorizationServerIds, actualLimit, actualOffset)
        val scopesResponse =
            ScopesResponse(
                scopes = scopes,
                page = page,
            )
        return Response
            .ok()
            .entity(scopesResponse)
            .build()
    }

    override fun updateScope(
        scopeId: UUID,
        scopeRequest: ScopeRequest,
    ): Response {
        // TODO: Implement updateScope in ScopeService
        return Response.status(Response.Status.NOT_IMPLEMENTED).build()
    }
}
