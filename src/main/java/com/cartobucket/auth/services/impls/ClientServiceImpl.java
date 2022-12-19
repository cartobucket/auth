package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.ClientService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;

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
    public ClientCode getClientCodeForEmailAndPassword(AuthorizationServer authorizationServer, String clientId, String email, String password, String nonce) {
        var client = clientRepository.findByClientId(clientId);
        if (client == null) {
            return null;
        }

        var user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }

        var matches = new BCryptPasswordEncoder().matches(password, user.getPasswordHash());
        if (!matches) {
            return null;
        }
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA3-256");

            var code = new BCryptPasswordEncoder().encode(new String(new SecureRandom().generateSeed(120)));
            var clientCode = new ClientCode();
            clientCode.setClientId(client.getId());
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

}
