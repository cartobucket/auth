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

open class Scopes(
    private val scopeService: ScopeService
) : ScopesApi {

    override fun createScope(scopeRequest: ScopeRequest): Response {
        return Response
            .ok()
            .entity(
                ScopeMapper.toResponse(
                    scopeService.createScope(ScopeMapper.to(scopeRequest))
                )
            )
            .build()
    }

    override fun deleteScope(scopeId: UUID): Response {
        scopeService.deleteScope(scopeId)
        return Response.noContent().build()
    }

    override fun getScope(scopeId: UUID): Response {
        return Response
            .ok()
            .entity(ScopeMapper.toResponse(scopeService.getScope(scopeId)))
            .build()
    }

    override fun listScopes(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val scopesResponse = ScopesResponse()
        scopesResponse.scopes = scopeService
            .getScopes(serverIds, Page(actualLimit, actualOffset))
            .map { ScopeMapper.toResponse(it) }
        scopesResponse.page = getPage("scopes", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(scopesResponse).build()
    }

    override fun updateScope(scopeId: UUID, scopeRequest: ScopeRequest): Response {
        // TODO: Implement updateScope in ScopeService
        return Response.status(Response.Status.NOT_IMPLEMENTED).build()
    }
}
