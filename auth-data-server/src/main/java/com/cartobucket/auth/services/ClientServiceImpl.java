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

package com.cartobucket.auth.services;

import com.cartobucket.auth.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.Client;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.PasswordAuthRequest;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class ClientServiceImpl implements ClientService {
    final UserRepository userRepository;
    final ClientRepository clientRepository;
    final ClientCodeRepository clientCodeRepository;
    final ScopeService scopeService;

    public ClientServiceImpl(UserRepository userRepository, ClientRepository clientRepository, ClientCodeRepository clientCodeRepository, ScopeService scopeService) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.clientCodeRepository = clientCodeRepository;
        this.scopeService = scopeService;
    }

    @Override
    @Transactional
    public ClientCode buildClientCodeForEmailAndPassword(
            AuthorizationServer authorizationServer,
            UUID clientId,
            String scopes,
            String redirectUri,
            String nonce,
            String state,
            String codeChallenge,
            String codeChallengeMethod,
            PasswordAuthRequest userAuthorizationRequest) {
        var client = clientRepository.findById(clientId);
        if (client.isEmpty() || !client.get().getAuthorizationServerId().equals(authorizationServer.getId())) {
            throw new CodeChallengeBadData("Unable to find the Client with the credentials provided");
        }

        var user = userRepository.findByUsername(userAuthorizationRequest.getUsername());
        if (user == null) {
            throw new CodeChallengeBadData("Unable to find the User with the credentials provided");
        }

        // Filter down to the scopes that are associated with the authorization server.
        var _scopes =  scopeService.filterScopesForAuthorizationServerId(
                authorizationServer.getId(),
                scopes
        );

        if (!new BCryptPasswordEncoder().matches(userAuthorizationRequest.getPassword(), user.getPasswordHash())) {
            throw new CodeChallengeBadData("Unable to find the User with the credentials provided");
        }
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            String code = new BigInteger(1, messageDigest
                    .digest(new SecureRandom()
                            .generateSeed(120)))
                    .toString(16);

            var clientCode = new ClientCode();
            clientCode.setClientId(client.get().getId());
            clientCode.setCode(code);
            clientCode.setRedirectUri(redirectUri);
            clientCode.setCreatedOn(OffsetDateTime.now());
            clientCode.setAuthorizationServerId(authorizationServer.getId());
            clientCode.setUserId(user.getId());
            clientCode.setNonce(nonce);
            clientCode.setState(state);
            clientCode.setCodeChallenge(codeChallenge);
            clientCode.setCodeChallengeMethod(codeChallengeMethod);
            clientCode.setScopes(_scopes);
            clientCodeRepository.save(clientCode);
            return clientCode;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteClient(final UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public Client getClient(final UUID clientId) {
        return clientRepository
                .findById(clientId)
                .orElseThrow(ClientNotFound::new);
    }

    @Override
    @Transactional
    public Client updateClient(final UUID clientId, final Client client) {
        var _client = clientRepository
                .findById(clientId)
                .orElseThrow(ClientNotFound::new);

        _client.setUpdatedOn(OffsetDateTime.now());
        _client.setScopes(client.getScopes());
        _client.setName(client.getName());
        _client.setRedirectUris(client.getRedirectUris());
        _client = clientRepository.save(_client);
        return _client;
    }

    @Override
    public List<Client> getClients(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return clientRepository
                    .findAllByAuthorizationServerIdIn(authorizationServerIds)
                    .stream()
                    .toList();

        } else {
            return StreamSupport
                    .stream(clientRepository.findAll().spliterator(), false)
                    .toList();
        }
    }

    @Override
    @Transactional
    public Client createClient(final Client client) {
        client.setCreatedOn(OffsetDateTime.now());
        client.setUpdatedOn(OffsetDateTime.now());
        return clientRepository.save(client);
    }
}
