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
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ClientService;
import com.cartobucket.auth.rpc.ClientCodeGetRequest;
import com.cartobucket.auth.rpc.ClientCodeResponse;
import com.cartobucket.auth.rpc.ClientCreateClientCodeRequest;
import com.cartobucket.auth.rpc.ClientCreateClientCodeResponse;
import com.cartobucket.auth.rpc.ClientCreateRequest;
import com.cartobucket.auth.rpc.ClientCreateResponse;
import com.cartobucket.auth.rpc.ClientDeleteRequest;
import com.cartobucket.auth.rpc.ClientGetRequest;
import com.cartobucket.auth.rpc.ClientListRequest;
import com.cartobucket.auth.rpc.ClientListResponse;
import com.cartobucket.auth.rpc.ClientResponse;
import com.cartobucket.auth.rpc.ClientUpdateRequest;
import com.cartobucket.auth.rpc.Clients;
import com.cartobucket.auth.data.services.grpc.mappers.server.MetadataMapper;
import com.cartobucket.auth.data.services.grpc.mappers.server.ScopeMapper;
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
            client.setScopes(request
                    .getScopeIdsList()
                    .stream()
                    .map(UUID::fromString)
                    .map(Scope::new)
                    .toList()
            );
            List<URI> list = new ArrayList<>();
            for (String s : request.getRedirectUrisList()) {
                list.add(new URI(s));
            }
            client.setRedirectUris(
                    list
            );
            client.setMetadata(MetadataMapper.from(request.getMetadata()));
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
                                    .addAllScopes(client.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                    .addAllRedirectUris(client
                                            .getRedirectUris()
                                            .stream()
                                            .map(String::valueOf)
                                            .toList()
                                    )
                                    .setMetadata(MetadataMapper.to(client.getMetadata()))
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
    public Uni<ClientCreateClientCodeResponse> createClientCode(ClientCreateClientCodeRequest request) {
        final var clientCode = new ClientCode();
        clientCode.setClientId(request.getClientId());
        clientCode.setAuthorizationServerId(UUID.fromString(request.getAuthorizationServerId()));
        clientCode.setRedirectUri(request.getRedirectUri());
        clientCode.setScopes(request.getScopesList().stream().map(ScopeMapper::toScope).toList());
        clientCode.setState(request.getState());
        clientCode.setCodeChallenge(request.getCodeChallenge());
        clientCode.setCodeChallengeMethod(request.getCodeChallengeMethod());
        clientCode.setNonce(request.getNonce());
        clientCode.setUserId(UUID.fromString(request.getUserId()));
        final var _code = clientService.createClientCode(clientCode.getAuthorizationServerId(), clientCode);
        return Uni
                .createFrom()
                .item(
                        ClientCreateClientCodeResponse
                                .newBuilder()
                                .setId(_code.getId().toString())
                                .setClientId(_code.getClientId())
                                .setAuthorizationServerId(_code.getAuthorizationServerId().toString())
                                .setRedirectUri(_code.getRedirectUri())
                                .addAllScopes(_code.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                .setState(_code.getState())
                                .setCodeChallenge(_code.getCodeChallenge())
                                .setCodeChallengeMethod(_code.getCodeChallengeMethod())
                                .setNonce(_code.getNonce())
                                .setUserId(String.valueOf(_code.getUserId()))
                                .setCode(_code.getCode())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ClientListResponse> listClients(ClientListRequest request) {
        final var clients = clientService.getClients(
                request
                        .getAuthorizationServerIdsList()
                        .stream()
                        .map(UUID::fromString)
                        .toList(),
                new Page(
                        Long.valueOf(request.getLimit()).intValue(),
                        Long.valueOf(request.getOffset()).intValue()
                )
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
                                                                .addAllScopes(client.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                                                .addAllRedirectUris(client
                                                                        .getRedirectUris()
                                                                        .stream()
                                                                        .map(String::valueOf)
                                                                        .toList()
                                                                )
                                                                .setMetadata(MetadataMapper.to(client.getMetadata()))
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
            client.setScopes(request.getScopesList().stream().map(ScopeMapper::toScope).toList());
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
                                    .addAllScopes(client.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                    .addAllRedirectUris(client
                                            .getRedirectUris()
                                            .stream()
                                            .map(String::valueOf)
                                            .toList()
                                    )
                                    .setMetadata(MetadataMapper.to(client.getMetadata()))
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
        final var client = clientService.getClient(request.getId());
        return Uni
                .createFrom()
                .item(
                        ClientResponse
                                .newBuilder()
                                .setId(String.valueOf(client.getId()))
                                .setAuthorizationServerId(String.valueOf(client.getAuthorizationServerId()))
                                .setName(client.getName())
                                .addAllScopes(client.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                .addAllRedirectUris(client
                                        .getRedirectUris()
                                        .stream()
                                        .map(String::valueOf)
                                        .toList()
                                )
                                .setMetadata(MetadataMapper.to(client.getMetadata()))
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(client.getCreatedOn().toEpochSecond()).build())
                                .setUpdatedOn(Timestamp.newBuilder().setSeconds(client.getUpdatedOn().toEpochSecond()).build())
                                .build()
                );
    }

    @Override
    @Blocking
    public Uni<ClientCodeResponse> getClientCode(ClientCodeGetRequest request) {
        final var clientCode = clientService.getClientCode(request.getCode());
        return Uni
                .createFrom()
                .item(
                        ClientCodeResponse
                                .newBuilder()
                                .setId(clientCode.getId().toString())
                                .setClientId(clientCode.getClientId())
                                .setAuthorizationServerId(clientCode.getAuthorizationServerId().toString())
                                .setRedirectUri(clientCode.getRedirectUri())
                                .addAllScopes(clientCode.getScopes().stream().map(ScopeMapper::toResponse).toList())
                                .setState(clientCode.getState())
                                .setCodeChallenge(clientCode.getCodeChallenge())
                                .setCodeChallengeMethod(clientCode.getCodeChallengeMethod())
                                .setNonce(clientCode.getNonce())
                                .setCode(clientCode.getCode())
                                .setCreatedOn(Timestamp.newBuilder().setSeconds(clientCode.getCreatedOn().toEpochSecond()).build())
                                .setUserId(clientCode.getUserId().toString())
                                .build()
                );
    }
}
