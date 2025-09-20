// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.ClientRequest
import com.cartobucket.auth.api.dto.ClientsResponse
import com.cartobucket.auth.api.interfaces.ClientsApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.ClientMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.ClientService
import jakarta.ws.rs.core.Response
import java.util.UUID

class Clients(
    private val clientService: ClientService,
) : ClientsApi {
    override fun createClient(clientRequest: ClientRequest): Response =
        Response
            .ok()
            .entity(
                ClientMapper.toResponse(
                    clientService.createClient(ClientMapper.to(clientRequest)),
                ),
            ).build()

    override fun deleteClient(clientId: UUID): Response {
        clientService.deleteClient(clientId)
        return Response.noContent().build()
    }

    override fun getClient(clientId: UUID): Response =
        Response
            .ok()
            // TODO: The clientId can be an arbitrary string.
            .entity(ClientMapper.toResponse(clientService.getClient(clientId.toString())))
            .build()

    override fun listClients(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val clients =
            clientService
                .getClients(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { ClientMapper.toResponse(it) }
        val page = getPage("clients", actualAuthorizationServerIds, actualLimit, actualOffset)
        val clientsResponse =
            ClientsResponse(
                clients = clients,
                page = page,
            )
        return Response
            .ok()
            .entity(clientsResponse)
            .build()
    }

    override fun updateClient(
        clientId: UUID,
        clientRequest: ClientRequest,
    ): Response =
        Response
            .ok()
            .entity(
                ClientMapper.toResponse(
                    clientService.updateClient(clientId, ClientMapper.to(clientRequest)),
                ),
            ).build()
}
