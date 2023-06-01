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

package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ClientsApi;
import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientsResponse;
import com.cartobucket.auth.routes.mappers.ClientMapper;
import com.cartobucket.auth.routes.mappers.ClientRequestFilterMapper;
import com.cartobucket.auth.services.ClientService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

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
                .entity(ClientMapper.toResponse(clientService.getClient(clientId)))
                .build();
    }

    @Override
    public Response listClients(List<UUID> authorizationServerIds) {
        final var clientsResponse = new ClientsResponse();
        clientsResponse.setClients(
                clientService.getClients(authorizationServerIds)
                        .stream()
                        .map(ClientMapper::toResponse)
                        .toList()
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
