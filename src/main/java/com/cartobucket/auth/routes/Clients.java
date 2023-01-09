package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.ClientsApi;
import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.model.generated.ClientsResponse;
import com.cartobucket.auth.services.ClientService;

import java.util.UUID;

public class Clients implements ClientsApi {
    final ClientService clientService;

    public Clients(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void clientsClientIdDelete(UUID clientId) {
        clientService.deleteClient(clientId);
    }

    @Override
    public ClientResponse clientsClientIdGet(UUID clientId) {
        return clientService.getClient(clientId);
    }

    @Override
    public ClientResponse clientsClientIdPut(UUID clientId, ClientRequest clientRequest) {
        return clientService.updateClient(clientId, clientRequest);
    }

    @Override
    public ClientsResponse clientsGet() {
        return clientService.getClients();
    }

    @Override
    public ClientResponse clientsPost(ClientRequest clientRequest) {
        return clientService.createClient(clientRequest);
    }
}
