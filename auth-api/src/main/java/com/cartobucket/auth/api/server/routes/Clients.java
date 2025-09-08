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

package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.api.interfaces.ClientsApi;
import com.cartobucket.auth.api.dto.ClientRequest;
import com.cartobucket.auth.api.dto.ClientsResponse;
import com.cartobucket.auth.api.server.routes.mappers.ClientMapper;
import com.cartobucket.auth.data.services.ClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Clients implements ClientsApi {
    final ClientService clientService;

    public Clients(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Response createClient(ClientRequest clientRequest) {
        return Response
                .ok()
                .entity(
                        ClientMapper.toResponse(
                                clientService.createClient(ClientMapper.to(clientRequest))
                        )
                )
                .build();
    }

    @Override
    public Response deleteClient(UUID clientId) {
        clientService.deleteClient(clientId);
        return Response
                .noContent()
                .build();
    }

    @Override
    public Response getClient(UUID clientId) {
        return Response
                .ok()
                // TODO: The clientId can be an arbitrary string.
                .entity(ClientMapper.toResponse(clientService.getClient(String.valueOf(clientId))))
                .build();
    }

    @Override
    public Response listClients(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        // TODO: Probably makes senes to move to Kotlin and use default parameters
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var clientsResponse = new ClientsResponse();
        clientsResponse.setClients(
                clientService.getClients(authorizationServerIds, new Page(limit, offset))
                        .stream()
                        .map(ClientMapper::toResponse)
                        .toList()
        );
        clientsResponse.setPage(
                getPage("clients", authorizationServerIds, limit, offset)
        );
        return Response
                .ok()
                .entity(clientsResponse)
                .build();
    }

    @Override
    public Response updateClient(UUID clientId, ClientRequest clientRequest) {
        return Response
                .ok()
                .entity(
                        ClientMapper.toResponse(
                                clientService.updateClient(clientId, ClientMapper.to(clientRequest))
                        )
                )
                .build();
    }
}
