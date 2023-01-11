package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;

import java.util.UUID;

public interface ClientService {
    ClientCode buildClientCodeForEmailAndPassword(
            AuthorizationServer authorizationServer,
            AuthorizationRequest authorizationRequest,
            UserAuthorizationRequest userAuthorizationRequest
    );

    void deleteClient(UUID clientId);

    ClientResponse getClient(UUID clientId);

    ClientResponse updateClient(UUID clientId, ClientRequest clientRequest);

    ClientsResponse getClients();

    ClientResponse createClient(ClientRequest clientRequest);
}
