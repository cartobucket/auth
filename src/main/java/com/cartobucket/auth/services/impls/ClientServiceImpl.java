package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.ClientRequest;
import com.cartobucket.auth.model.generated.ClientResponse;
import com.cartobucket.auth.model.generated.ClientsResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.mappers.ClientMapper;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.ClientService;
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

@ApplicationScoped
public class ClientServiceImpl implements ClientService {
    final UserRepository userRepository;
    final ClientRepository clientRepository;
    private ClientCodeRepository clientCodeRepository;

    public ClientServiceImpl(UserRepository userRepository, ClientRepository clientRepository, ClientCodeRepository clientCodeRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.clientCodeRepository = clientCodeRepository;
    }

    @Override
    public ClientCode buildClientCodeForEmailAndPassword(AuthorizationServer authorizationServer, String clientId, String email, String password, String nonce) {
        var client = clientRepository.findById(UUID.fromString(clientId));
        if (client.isEmpty() || !client.get().getAuthorizationServerId().equals(authorizationServer.getId())) {
            throw new BadRequestException("Unable to find the Client with the credentials provided");
        }

        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BadRequestException("Unable to find the User with the credentials provided");
        }

        var matches = new BCryptPasswordEncoder().matches(password, user.getPasswordHash());
        if (!matches) {
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
            clientCode.setCreatedOn(OffsetDateTime.now());
            clientCode.setAuthorizationServerId(authorizationServer.getId());
            clientCode.setUserId(user.getId());
            clientCode.setNonce(nonce);
            clientCodeRepository.save(clientCode);
            return clientCode;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteClient(UUID clientId) {

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
    public ClientResponse updateClient(UUID clientId, ClientRequest clientRequest) {
        final var client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new NotFoundException();
        }
        var _client = ClientMapper.to(clientRequest);
        _client.setId(client.get().getId());
        _client = clientRepository.save(_client);
        return ClientMapper.toResponse(_client);
    }

    @Override
    public ClientsResponse getClients() {
        var clients = StreamSupport
                .stream(clientRepository.findAll().spliterator(), false)
                .map(ClientMapper::toResponse)
                .toList();
        var clientsResponse = new ClientsResponse();
        clientsResponse.setClients(clients);
        return clientsResponse;
    }

    @Override
    public ClientResponse createClient(ClientRequest clientRequest) {
        return ClientMapper.toResponse(clientRepository.save(ClientMapper.to(clientRequest)));
    }

}
