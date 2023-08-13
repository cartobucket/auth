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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.data.services.impls.mappers.ClientsMapper;
import com.cartobucket.auth.rpc.ClientDeleteRequest;
import com.cartobucket.auth.rpc.ClientGetRequest;
import com.cartobucket.auth.rpc.ClientListRequest;
import com.cartobucket.auth.rpc.ClientUpdateRequest;
import com.cartobucket.auth.rpc.MutinyAuthorizationServersGrpc;
import com.cartobucket.auth.rpc.MutinyClientsGrpc;
import io.quarkus.arc.DefaultBean;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class ClientService implements com.cartobucket.auth.data.services.ClientService {

    @Inject
    @GrpcClient("clients")
    MutinyClientsGrpc.MutinyClientsStub clientsClient;

    @Inject
    @GrpcClient("authorizationServers")
    MutinyAuthorizationServersGrpc.MutinyAuthorizationServersStub authorizationServersClient;

    @Override
    public ClientCode createClientCode(
            UUID authorizationServerId,
            ClientCode clientCode
    ) throws CodeChallengeBadData {
        return new ClientCode();
    }

    @Override
    public void deleteClient(UUID clientId) {
        clientsClient.deleteClient(
                ClientDeleteRequest
                        .newBuilder()
                        .setId(String.valueOf(clientId))
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS));
    }

    @Override
    public Client getClient(String clientId) throws ClientNotFound {
        return ClientsMapper.to(
                clientsClient
                    .getClient(
                            ClientGetRequest
                                    .newBuilder()
                                    .setId(clientId)
                                    .build()
                    )
                    .await()
                    .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public Client updateClient(UUID clientId, Client client) throws ClientNotFound {
        return ClientsMapper.to(
                clientsClient
                        .updateClient(
                                ClientUpdateRequest
                                        .newBuilder()
                                        .setId(String.valueOf(clientId))
                                        .setName(client.getName())
                                        .addAllScopes(client.getScopes())
                                        .addAllRedirectUris(client.getRedirectUris().stream().map(String::valueOf).toList())
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }

    @Override
    public List<Client> getClients(List<UUID> authorizationServerIds) {
        return clientsClient.listClients(
                ClientListRequest
                        .newBuilder()
                        .addAllAuthorizationServerIds(
                                authorizationServerIds
                                        .stream()
                                        .map(String::valueOf)
                                        .toList()
                        )
                        .build()
                )
                .await()
                .atMost(Duration.of(3, ChronoUnit.SECONDS))
                .getClientsList()
                .stream()
                .map(ClientsMapper::to)
                .toList();
    }

    @Override
    public Client createClient(Client client) {
        return ClientsMapper.to(
                clientsClient
                        .createClient(
                                com.cartobucket.auth.rpc.ClientCreateRequest
                                        .newBuilder()
                                        .setName(client.getName())
                                        .addAllScopes(client.getScopes())
                                        .addAllRedirectUris(client.getRedirectUris().stream().map(String::valueOf).toList())
                                        .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                        .build()
                        )
                        .await()
                        .atMost(Duration.of(3, ChronoUnit.SECONDS))
        );
    }
}
