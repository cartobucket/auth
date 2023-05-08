package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.AuthorizationRequest;
import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientRequestFilter;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.model.generated.ClientsResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.PasswordAuthRequest;

import java.util.UUID;

public interface ClientService {
    ClientCode buildClientCodeForEmailAndPassword(
            AuthorizationServer authorizationServer,
            AuthorizationRequest authorizationRequest,
            PasswordAuthRequest userAuthorizationRequest
    );

    void deleteClient(UUID clientId);

    ClientResponse getClient(UUID clientId);

    ClientResponse updateClient(UUID clientId, ClientRequest clientRequest);

    ClientsResponse getClients(ClientRequestFilter filter);

    ClientResponse createClient(ClientRequest clientRequest);
}
