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

package com.cartobucket.auth.rpc.server.services;

import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientCodeNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.rpc.server.entities.mappers.ClientCodeMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ClientMapper;
import com.cartobucket.auth.rpc.server.repositories.ClientCodeRepository;
import com.cartobucket.auth.rpc.server.repositories.ClientRepository;
import com.cartobucket.auth.rpc.server.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ClientService implements com.cartobucket.auth.data.services.ClientService {
    final ClientRepository clientRepository;
    final ClientCodeRepository clientCodeRepository;
    final ScopeService scopeService;

    public ClientService(
            ClientRepository clientRepository,
            ClientCodeRepository clientCodeRepository,
            ScopeService scopeService) {
        this.clientRepository = clientRepository;
        this.clientCodeRepository = clientCodeRepository;
        this.scopeService = scopeService;
    }

    @Override
    @Transactional
    public ClientCode createClientCode(
            UUID authorizationServerId,
            ClientCode clientCode
    ) {
        var client = clientRepository.findByClientId(clientCode.getClientId());
        if (client.isEmpty() || !client.get().getAuthorizationServerId().equals(authorizationServerId)) {
            throw new CodeChallengeBadData("Unable to find the Client with the credentials provided");
        }

        // Filter down to the scopes that are associated with the authorization server.
        var _scopes = scopeService.filterScopesForAuthorizationServerId(
                authorizationServerId,
                ScopeService.scopeListToScopeString(clientCode.getScopes())
        );
        clientCode.setScopes(_scopes);
        var _clientCode = ClientCodeMapper.to(clientCode);
        clientCodeRepository.persist(_clientCode);

        return ClientCodeMapper.from(_clientCode);
    }

    @Override
    @Transactional
    public void deleteClient(final UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public Client getClient(final String clientId) throws ClientNotFound {
        return clientRepository
                .findByClientId(clientId)
                .map(ClientMapper::from)
                .orElseThrow(ClientNotFound::new);
    }

    @Override
    public ClientCode getClientCode(String clientCode) throws ClientCodeNotFound {
        return clientCodeRepository.findByCode(clientCode)
                .map(ClientCodeMapper::from)
                .orElseThrow(ClientCodeNotFound::new);
    }

    @Override
    @Transactional
    public Client updateClient(final UUID clientId, final Client client) throws ClientNotFound {
        var _client = clientRepository
                .findByIdOptional(clientId)
                .orElseThrow(ClientNotFound::new);

        _client.setUpdatedOn(OffsetDateTime.now());
        _client.setScopes(client.getScopes());
        _client.setName(client.getName());
        _client.setRedirectUris(client.getRedirectUris());
        clientRepository.persist(_client);
        return ClientMapper.from(_client);
    }

    @Override
    public List<Client> getClients(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return clientRepository
                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                    .stream()
                    .map(ClientMapper::from)
                    .toList();

        } else {
            return clientRepository
                    .listAll()
                    .stream()
                    .map(ClientMapper::from)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Client createClient(final Client client) {
        client.setCreatedOn(OffsetDateTime.now());
        client.setUpdatedOn(OffsetDateTime.now());
        var _client = ClientMapper.to(client);
        clientRepository.persist(_client);
        return ClientMapper.from(_client);
    }
}
