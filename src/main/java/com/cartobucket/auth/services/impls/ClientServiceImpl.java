package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.AuthorizationRequest;
import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientRequestFilter;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.model.generated.ClientsResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.PasswordAuthRequest;
import com.cartobucket.auth.models.mappers.ClientMapper;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.ClientService;
import com.cartobucket.auth.services.ScopeService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;

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
            AuthorizationRequest authorizationRequest,
            PasswordAuthRequest userAuthorizationRequest) {
        var client = clientRepository.findById(UUID.fromString(authorizationRequest.getClientId()));
        if (client.isEmpty() || !client.get().getAuthorizationServerId().equals(authorizationServer.getId())) {
            throw new BadRequestException("Unable to find the Client with the credentials provided");
        }

        var user = userRepository.findByUsername(userAuthorizationRequest.getUsername());
        if (user == null) {
            throw new BadRequestException("Unable to find the User with the credentials provided");
        }

        // Filter down to the scopes that are associated with the authorization server.
        var scopes =  scopeService.filterScopesForAuthorizationServerId(
                authorizationServer.getId(),
                authorizationRequest.getScope());

        if (!new BCryptPasswordEncoder().matches(userAuthorizationRequest.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Unable to find the User with the credentials provided");
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
            clientCode.setRedirectUri(authorizationRequest.getRedirectUri());
            clientCode.setCreatedOn(OffsetDateTime.now());
            clientCode.setAuthorizationServerId(authorizationServer.getId());
            clientCode.setUserId(user.getId());
            clientCode.setNonce(authorizationRequest.getNonce());
            clientCode.setState(authorizationRequest.getState());
            clientCode.setCodeChallenge(authorizationRequest.getCodeChallenge());
            clientCode.setCodeChallengeMethod(String.valueOf(authorizationRequest.getCodeChallengeMethod()));
            clientCode.setScopes(scopes);
            clientCodeRepository.save(clientCode);
            return clientCode;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteClient(UUID clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public ClientResponse getClient(UUID clientId) {
        final var client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new NotFoundException();
        }
        return ClientMapper.toResponse(client.get());
    }

    @Override
    @Transactional
    public ClientResponse updateClient(UUID clientId, ClientRequest clientRequest) {
        final var client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new NotFoundException();
        }
        var _client = ClientMapper.to(clientRequest);
        _client.setUpdatedOn(OffsetDateTime.now());
        _client.setId(client.get().getId());
        _client = clientRepository.save(_client);
        return ClientMapper.toResponse(_client);
    }

    @Override
    public ClientsResponse getClients(ClientRequestFilter filter) {
        var clients = StreamSupport.stream(clientRepository.findAll().spliterator(), false);

        // TODO: This should happen in the DB.
        if (!filter.getAuthorizationServerIds().isEmpty()) {
            clients = clients.filter(client -> filter.getAuthorizationServerIds().contains(client.getAuthorizationServerId()));
        }

        var clientsResponse = new ClientsResponse();
        clientsResponse.setClients(clients.map(ClientMapper::toResponse).toList());
        return clientsResponse;
    }

    @Override
    @Transactional
    public ClientResponse createClient(ClientRequest clientRequest) {
        var client = ClientMapper.to(clientRequest);
        client.setCreatedOn(OffsetDateTime.now());
        client.setUpdatedOn(OffsetDateTime.now());
        return ClientMapper.toResponse(clientRepository.save(client));
    }

}
