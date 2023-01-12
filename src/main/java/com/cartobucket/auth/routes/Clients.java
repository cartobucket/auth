package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ClientsApi;
import com.cartobucket.auth.model.generated.ClientRequest;
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
    public Response clientsClientIdDelete(UUID clientId) {
        clientService.deleteClient(clientId);
        return Response.ok().build();
    }

    @Override
    public Response clientsClientIdGet(UUID clientId) {
        return Response.ok().entity(clientService.getClient(clientId)).build();
    }

    @Override
    public Response clientsClientIdPut(UUID clientId, ClientRequest clientRequest) {
        return Response.ok().entity(clientService.updateClient(clientId, clientRequest)).build();
    }

    @Override
    public Response clientsGet(List<UUID> authorizationServerIds) {
        return Response
                .ok()
                .entity(clientService.getClients(ClientRequestFilterMapper.to(authorizationServerIds)))
                .build();
    }

    @Override
    public Response clientsPost(ClientRequest clientRequest) {
        return Response.ok().entity(clientService.createClient(clientRequest)).build();
    }
}
