// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ScopeRequest
import com.cartobucket.auth.api.dto.ScopeResponse
import com.cartobucket.auth.data.domain.AuthorizationServer
import com.cartobucket.auth.data.domain.Scope

object ScopeMapper {
    fun to(scopeRequest: ScopeRequest): Scope {
        val scope = Scope()
        scope.name = scopeRequest.name
        val authorizationServer = AuthorizationServer()
        authorizationServer.id = scopeRequest.authorizationServerId
        scope.authorizationServer = authorizationServer
        scope.metadata = MetadataMapper.from(scopeRequest.metadata)
        return scope
    }

    fun toResponse(scope: Scope): ScopeResponse =
        ScopeResponse(
            id = scope.id.toString(),
            authorizationServerId = scope.authorizationServer?.id,
            name = scope.name,
            metadata = MetadataMapper.to(scope.metadata),
            createdOn = scope.createdOn,
            updatedOn = scope.updatedOn,
        )

    fun toSummaryResponse(scope: Scope): ScopeResponse =
        ScopeResponse(
            id = scope.id.toString(),
            authorizationServerId = null,
            name = scope.name,
            metadata = null,
            createdOn = null,
            updatedOn = null,
        )
}
