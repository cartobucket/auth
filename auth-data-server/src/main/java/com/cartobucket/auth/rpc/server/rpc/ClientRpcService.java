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

package com.cartobucket.auth.rpc.server.rpc;


import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.services.ClientService;
import com.cartobucket.auth.rpc.ClientCreateRequest;
import com.cartobucket.auth.rpc.ClientCreateResponse;
import com.cartobucket.auth.rpc.ClientDeleteRequest;
import com.cartobucket.auth.rpc.ClientGetRequest;
import com.cartobucket.auth.rpc.ClientListRequest;
import com.cartobucket.auth.rpc.ClientListResponse;
import com.cartobucket.auth.rpc.ClientResponse;
import com.cartobucket.auth.rpc.ClientUpdateRequest;
import com.cartobucket.auth.rpc.Clients;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
public class ClientRpcService implements Clients {
    final ClientService clientService;

    public ClientRpcService(ClientService applicationService) {
        this.clientService = applicationService;
    }

    @Override
    @Blocking
    public Uni<ClientCreateResponse> createClient(ClientCreateRequest request) {
        try {
            var client = new Client();
            client.setName(request.getName());
            client.setScopes(request.getScopesList());
            List<URI> list = new ArrayList<>();
            for (String s : request.getRedirectUrisList()) {
                list.add(new URI(s));
            }
            client.setRedirectUris(
                    list
            );
            client.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
            client = clientService.createClient(client);
            return Uni
                    .createFrom()
                    .item(
                            ClientCreateResponse
                                    .newBuilder()
                                    .setId(String.valueOf(client.getId()))
                                    .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                    .setName(client.getName())
                                    .addAllScopes(client.getScopes())
                                    .addAllRedirectUris(client
                                            .getRedirectUris()
                                            .stream()
                                            .map(String::valueOf)
                                            .toList()
                                    )
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(client.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(client.getUpdatedOn().toEpochSecond()).build())
                                    .build()
                    );

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Blocking
    public Uni<ClientListResponse> listClients(ClientListRequest request) {
        final var clients = clientService.getClients(
                request
                        .getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList()
        );
        return Uni
                .createFrom()
                .item(
                        ClientListResponse
                                .newBuilder()
                                .addAllClients(
                                        clients
                                                .stream()
                                                .map(
                                                        client -> ClientResponse
                                                                .newBuilder()
                                                                .setId(String.valueOf(client.getId()))
                                                                .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                                                .setName(client.getName())
                                                                .addAllScopes(client.getScopes())
                                                                .addAllRedirectUris(client
                                                                        .getRedirectUris()
                                                                        .stream()
                                                                        .map(String::valueOf)
                                                                        .toList()
                                                                )
                                                                .setCreatedOn(Timestamp.newBuilder().setSeconds(client.getCreatedOn().toEpochSecond()).build())
                                                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(client.getUpdatedOn().toEpochSecond()).build())
                                                                .build())
                                                .toList()
                                )
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ClientResponse> deleteClient(ClientDeleteRequest request) {
        clientService.deleteClient(UUID.fromString(request.getId()));
        return Uni
                .createFrom()
                .item(
                        ClientResponse
                                .newBuilder()
                                .setId(request.getId())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ClientResponse> updateClient(ClientUpdateRequest request) {
        try {
            var client = new Client();
            client.setName(request.getName());
            client.setScopes(request.getScopesList());
            List<URI> list = new ArrayList<>();
            for (String s : request.getRedirectUrisList()) {
                list.add(new URI(s));
            }
            client.setRedirectUris(
                    list
            );
            client = clientService.updateClient(UUID.fromString(request.getId()), client);
            return Uni
                    .createFrom()
                    .item(
                            ClientResponse
                                    .newBuilder()
                                    .setId(String.valueOf(client.getId()))
                                    .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                    .setName(client.getName())
                                    .addAllScopes(client.getScopes())
                                    .addAllRedirectUris(client
                                            .getRedirectUris()
                                            .stream()
                                            .map(String::valueOf)
                                            .toList()
                                    )
                                    .setCreatedOn(Timestamp.newBuilder().setSeconds(client.getCreatedOn().toEpochSecond()).build())
                                    .setUpdatedOn(Timestamp.newBuilder().setSeconds(client.getUpdatedOn().toEpochSecond()).build())
                                    .build()
                    );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Blocking
    public Uni<ClientResponse> getClient(ClientGetRequest request) {
        final var client = clientService.getClient(UUID.fromString(request.getId()));
        return Uni
                .createFrom()
                .item(
                        ClientResponse
                                .newBuilder()
                                .setId(String.valueOf(client.getId()))
                                .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                .setName(client.getName())
                                .addAllScopes(client.getScopes())
                                .addAllRedirectUris(client
                                        .getRedirectUris()
                                        .stream()
                                        .map(String::valueOf)
                                        .toList()
                                )
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(client.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(client.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }
}
