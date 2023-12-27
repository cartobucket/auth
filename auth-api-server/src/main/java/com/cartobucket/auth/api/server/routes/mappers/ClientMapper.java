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

package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.services.ScopeService;

import java.net.URI;

public class ClientMapper {
    public static Client to(ClientRequest clientRequest) {
        var client = new Client();
        client.setName(clientRequest.getName());
        client.setAuthorizationServerId(clientRequest.getAuthorizationServerId());
        client.setRedirectUris(clientRequest.getRedirectUris().stream().map(URI::create).toList());
        client.setScopes(ScopeService.scopeStringToScopeList(clientRequest.getScopes()));
        client.setMetadata(MetadataMapper.from(clientRequest.getMetadata()));
        return client;
    }

    public static ClientResponse toResponse(Client client) {
        var clientResponse = new ClientResponse();
        clientResponse.setId(String.valueOf(client.getId()));
        clientResponse.setName(client.getName());
        clientResponse.setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()));
        clientResponse.setRedirectUris(client.getRedirectUris().stream().map(String::valueOf).toList());
        clientResponse.setScopes(
                ScopeService.scopeListToScopeString(
                        client
                                .getScopes()
                                .stream()
                                .map(Scope::getName)
                                .toList()
                )
        );
        clientResponse.setMetadata(MetadataMapper.to(client.getMetadata()));
        clientResponse.setCreatedOn(client.getCreatedOn());
        clientResponse.setUpdatedOn(client.getUpdatedOn());
        return clientResponse;
    }
}
