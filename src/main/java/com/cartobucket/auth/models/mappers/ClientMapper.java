package com.cartobucket.auth.models.mappers;

import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.models.Client;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ClientMapper {
    public static Client to(ClientRequest clientRequest) {
        var client = new Client();
        client.setName(clientRequest.getName());
        client.setAuthorizationServerId(UUID.fromString(clientRequest.getAuthorizationServerId()));
        client.setRedirectUris(clientRequest.getRedirectUris().stream().map(URI::create).toList());
        client.setCreatedOn(OffsetDateTime.now());
        client.setUpdatedOn(OffsetDateTime.now());
        return client;
    }

    public static ClientResponse toResponse(Client client) {
        var clientResponse = new ClientResponse();
        clientResponse.setId(String.valueOf(client.getId()));
        clientResponse.setName(client.getName());
        clientResponse.setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()));
        clientResponse.setRedirectUris(client.getRedirectUris().stream().map(String::valueOf).toList());
        clientResponse.setCreatedOn(client.getCreatedOn());
        clientResponse.setUpdatedOn(client.getUpdatedOn());
        return clientResponse;
    }
}
