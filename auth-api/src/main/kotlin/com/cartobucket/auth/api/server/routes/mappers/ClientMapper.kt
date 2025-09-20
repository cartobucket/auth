// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ClientRequest
import com.cartobucket.auth.api.dto.ClientResponse
import com.cartobucket.auth.data.domain.Client
import com.cartobucket.auth.data.domain.Scope
import java.net.URI

object ClientMapper {
    fun to(clientRequest: ClientRequest): Client {
        val client = Client()
        client.name = clientRequest.name
        client.authorizationServerId = clientRequest.authorizationServerId
        client.redirectUris = clientRequest.redirectUris?.map { URI.create(it) }
        client.scopes = clientRequest.scopes?.map { Scope(it) }
        client.metadata = MetadataMapper.from(clientRequest.metadata)
        return client
    }

    fun toResponse(client: Client): ClientResponse =
        ClientResponse(
            id = client.id?.toString(),
            name = client.name,
            authorizationServerId = client.authorizationServerId,
            redirectUris = client.redirectUris?.map { it.toString() } ?: emptyList(),
            scopes = client.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList(),
            metadata = MetadataMapper.to(client.metadata),
            createdOn = client.createdOn,
            updatedOn = client.updatedOn,
        )
}
