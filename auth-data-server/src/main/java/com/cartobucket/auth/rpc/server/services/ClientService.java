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
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientCodeNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.rpc.server.entities.EventType;
import com.cartobucket.auth.rpc.server.entities.ScopeReference;
import com.cartobucket.auth.rpc.server.entities.mappers.ClientCodeMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ClientMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ScopeMapper;
import com.cartobucket.auth.rpc.server.repositories.ClientCodeRepository;
import com.cartobucket.auth.rpc.server.repositories.ClientRepository;
import com.cartobucket.auth.rpc.server.repositories.EventRepository;
import com.cartobucket.auth.rpc.server.repositories.ScopeReferenceRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ClientService implements com.cartobucket.auth.data.services.ClientService {
    final ClientRepository clientRepository;
    final ClientCodeRepository clientCodeRepository;
    final ScopeReferenceRepository scopeReferenceRepository;
    final EventRepository eventRepository;
    final ScopeService scopeService;

    public ClientService(
            ClientRepository clientRepository,
            ClientCodeRepository clientCodeRepository,
            ScopeReferenceRepository scopeReferenceRepository, EventRepository eventRepository,
            ScopeService scopeService) {
        this.clientRepository = clientRepository;
        this.clientCodeRepository = clientCodeRepository;
        this.scopeReferenceRepository = scopeReferenceRepository;
        this.eventRepository = eventRepository;
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
                ScopeService.scopeListToScopeString(clientCode.getScopes().stream().map(com.cartobucket.auth.data.domain.Scope::getName).toList())
        );

        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        String code = new BigInteger(1, messageDigest
                .digest(new SecureRandom()
                        .generateSeed(120)))
                .toString(16);
        clientCode.setCode(code);

        clientCode.setScopes(_scopes);
        clientCode.setCreatedOn(OffsetDateTime.now());
        var _clientCode = ClientCodeMapper.to(clientCode);
        clientCodeRepository.persist(_clientCode);
        clientCode = ClientCodeMapper.from(_clientCode);
        clientCode.setScopes(scopeService.getScopesForResourceId(clientCode.getId()));

        eventRepository.createClientCodeEvent(clientCode, EventType.CREATE);

        return clientCode;
    }

    @Override
    @Transactional
    public void deleteClient(final UUID clientId) {
        final var client = clientRepository
                .findByIdOptional(clientId)
                .orElseThrow(ClientNotFound::new);
        clientRepository.delete(client);
        eventRepository.createClientEvent(ClientMapper.from(client), EventType.DELETE);
    }

    @Override
    @Transactional
    public Client updateClient(final UUID clientId, final Client client) throws ClientNotFound {
        var _client = clientRepository
                .findByIdOptional(clientId)
                .orElseThrow(ClientNotFound::new);

        _client.setUpdatedOn(OffsetDateTime.now());
        _client.setScopes(client.getScopes().stream().map(ScopeMapper::to).toList());
        _client.setName(client.getName());
        _client.setRedirectUris(client.getRedirectUris());
        clientRepository.persist(_client);
        eventRepository.createClientEvent(ClientMapper.from(_client), EventType.UPDATE);
        return ClientMapper.from(_client);
    }

    @Override
    @Transactional
    public Client getClient(final String clientId) throws ClientNotFound {
        return clientRepository
                .findByClientId(clientId)
                .map(ClientMapper::from)
                .orElseThrow(ClientNotFound::new);
    }

    @Override
    @Transactional
    public ClientCode getClientCode(String clientCode) throws ClientCodeNotFound {
        final var code = clientCodeRepository.findByCode(clientCode);
        return code
                .map(ClientCodeMapper::from)
                .orElseThrow(ClientCodeNotFound::new);
    }

    @Override
    @Transactional
    public List<Client> getClients(final List<UUID> authorizationServerIds, Page page) {
        if (!authorizationServerIds.isEmpty()) {
            return clientRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(ClientMapper::from)
                    .toList();

        } else {
            return clientRepository
                    .findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(ClientMapper::from)
                    .toList();
        }
    }

    @Override
    public Client createClient(final Client client) {
        QuarkusTransaction.begin();
        client.setCreatedOn(OffsetDateTime.now());
        client.setUpdatedOn(OffsetDateTime.now());
        client.setId(UUID.randomUUID());
        var _client = ClientMapper.to(client);
        clientRepository.persist(_client);

        final var scopeReferences = client
                .getScopes()
                .stream()
                .map(scope -> {
                    final var scopeReference = new ScopeReference();
                    scopeReference.setResourceId(_client.getId());
                    scopeReference.setScopeId(scope.getId());
                    scopeReference.setScopeReferenceType(ScopeReference.ScopeReferenceType.CLIENT);
                    return scopeReference;
                })
                .toList();
        scopeReferenceRepository.persist(scopeReferences);
        QuarkusTransaction.commit();

        QuarkusTransaction.begin();
        final var refreshedClient = ClientMapper.from(clientRepository.findById(_client.getId()));
        eventRepository.createClientEvent(refreshedClient, EventType.CREATE);
        QuarkusTransaction.commit();
        return refreshedClient;
    }
}
