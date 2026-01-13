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

import com.revethq.auth.core.api.dto.ClientRequest
import com.revethq.auth.core.api.dto.ClientsResponse
import com.revethq.auth.core.api.interfaces.ClientsApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.Pagination.getPage
import com.revethq.auth.web.api.routes.mappers.ClientMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.ClientService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class Clients @Inject constructor(
    private val clientService: ClientService
) : ClientsApi {

    override fun createClient(clientRequest: ClientRequest): Response {
        return Response
            .ok()
            .entity(
                ClientMapper.toResponse(
                    clientService.createClient(ClientMapper.to(clientRequest))
                )
            )
            .build()
    }

    override fun deleteClient(clientId: UUID): Response {
        clientService.deleteClient(clientId)
        return Response.noContent().build()
    }

    override fun getClient(clientId: UUID): Response {
        return Response
            .ok()
            // TODO: The clientId can be an arbitrary string.
            .entity(ClientMapper.toResponse(clientService.getClient(clientId.toString())))
            .build()
    }

    override fun listClients(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val clientsResponse = ClientsResponse()
        clientsResponse.clients = clientService
            .getClients(serverIds, Page(actualLimit, actualOffset))
            .map { ClientMapper.toResponse(it) }
        clientsResponse.page = getPage("clients", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(clientsResponse).build()
    }

    override fun updateClient(clientId: UUID, clientRequest: ClientRequest): Response {
        return Response
            .ok()
            .entity(
                ClientMapper.toResponse(
                    clientService.updateClient(clientId, ClientMapper.to(clientRequest))
                )
            )
            .build()
    }
}
