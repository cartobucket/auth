// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.AuthorizationServerRequest
import com.cartobucket.auth.api.dto.AuthorizationServerResponse
import com.cartobucket.auth.data.domain.AuthorizationServer
import java.net.URI

object AuthorizationServerMapper {
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

    fun toResponse(authorizationServer: AuthorizationServer): AuthorizationServerResponse =
        AuthorizationServerResponse(
            id = authorizationServer.id?.toString(),
            name = authorizationServer.name!!, // Required field
            serverUrl = authorizationServer.serverUrl?.toString(),
            audience = authorizationServer.audience,
            authorizationCodeTokenExpiration = authorizationServer.authorizationCodeTokenExpiration?.toInt(),
            clientCredentialsTokenExpiration = authorizationServer.clientCredentialsTokenExpiration?.toInt(),
            metadata = MetadataMapper.to(authorizationServer.metadata),
            scopes = authorizationServer.scopes?.map { ScopeMapper.toSummaryResponse(it) },
        )
}
