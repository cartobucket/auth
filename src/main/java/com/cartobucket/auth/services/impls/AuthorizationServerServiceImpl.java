package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import com.cartobucket.auth.models.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.SingingKeyRepository;
import com.cartobucket.auth.services.AuthorizationServerService;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.security.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class AuthorizationServerServiceImpl implements AuthorizationServerService {
    final AuthorizationServerRepository authorizationServerRepository;
    final SingingKeyRepository singingKeyRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }

    public AuthorizationServerServiceImpl(AuthorizationServerRepository authorizationServerRepository, SingingKeyRepository singingKeyRepository) {
        this.authorizationServerRepository = authorizationServerRepository;
        this.singingKeyRepository = singingKeyRepository;
    }

    @Override
    public JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer) {
        try {
            var keys = singingKeyRepository.findAllByAuthorizationServerId(
                    authorizationServer.getId()
            );
            var key =
                    keys.stream()
                            .findFirst()
                            .orElseThrow();

            return buildJwk(key);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static SigningKey generateSigningKey(AuthorizationServer authorizationServer) {
        try {
            var singingKey = new SigningKey();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            StringBuilder publicKey = new StringBuilder();
            publicKey.append("-----BEGIN PUBLIC KEY-----\n");
            publicKey.append(Base64.getMimeEncoder().encodeToString(pair.getPublic().getEncoded()));
            publicKey.append("\n-----END PUBLIC KEY-----\n");
            singingKey.setPublicKey(publicKey.toString());

            StringBuilder privateKey = new StringBuilder();
            privateKey.append("-----BEGIN PRIVATE KEY-----\n");
            privateKey.append(Base64.getMimeEncoder().encodeToString(pair.getPrivate().getEncoded()));
            privateKey.append("\n-----END PRIVATE KEY-----\n");
            singingKey.setPrivateKey(privateKey.toString());

            singingKey.setKeyType("RSA");
            singingKey.setAuthorizationServerId(authorizationServer.getId());
            singingKey.setMetadata(new HashMap<>());
            singingKey.setCreatedOn(OffsetDateTime.now());
            singingKey.setUpdatedOn(OffsetDateTime.now());

            return singingKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JWKS getJwksForAuthorizationServer(AuthorizationServer authorizationServer) {
        var singingKeys = singingKeyRepository.findAllByAuthorizationServerId(authorizationServer.getId());
        try {
            List<JWK> keys = new ArrayList<>();
            for (var key : singingKeys) {
                keys.add(buildJwk(key));
            }
            var jwks = new JWKS();
            jwks.setKeys(keys);
            return jwks;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrivateKey getSingingKeyForAuthorizationServer(AuthorizationServer authorizationServer) {
        var singingKeys = singingKeyRepository.findAllByAuthorizationServerId(
                authorizationServer.getId()
        );
        var key = singingKeys.stream().findFirst().orElseThrow();
        try {
            return KeyUtils.decodePrivateKey(key.getPrivateKey());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthorizationServerResponse createAuthorizationServer(AuthorizationServerRequest authorizationServerRequest) {
        var authorizationServer = AuthorizationServerMapper.from(authorizationServerRequest);
        authorizationServer.setCreatedOn(OffsetDateTime.now());
        authorizationServer.setUpdatedOn(OffsetDateTime.now());

        authorizationServer = authorizationServerRepository.save(authorizationServer);
        generateSigningKey(authorizationServer);

        return AuthorizationServerMapper.toResponse(authorizationServer);
    }

    @Override
    public AuthorizationServer getAuthorizationServer(UUID authorizationServerId) {
        final var authorizationServer = authorizationServerRepository.findById(authorizationServerId);
        if (authorizationServer.isEmpty()) {
            throw new NotFoundException("An Authorization Server with that id could not be found");
        }
        return authorizationServer.get();
    }

    @Override
    public AuthorizationServerResponse updateAuthorizationServer(UUID authorizationServerId, AuthorizationServerRequest authorizationServerRequest) {
        var authServer = authorizationServerRepository.findById(authorizationServerId);
        if (authServer.isEmpty()) {
            throw new NotFoundException();
        }
        var authorizationServer = AuthorizationServerMapper.from(authorizationServerRequest);
        authorizationServer.setId(authorizationServerId);
        authorizationServer.setCreatedOn(authServer.get().getCreatedOn());
        authorizationServer.setUpdatedOn(OffsetDateTime.now());
        authorizationServerRepository.save(authorizationServer);
        return AuthorizationServerMapper.toResponse(authorizationServer);
    }

    @Override
    public AuthorizationServersResponse getAuthorizationServers() {
        var response = new AuthorizationServersResponse();
        response.setAuthorizationServers(
                StreamSupport
                        .stream(authorizationServerRepository.findAll().spliterator(), false)
                        .map(AuthorizationServerMapper::toResponse)
                        .toList()
        );
        return response;
    }

    @Override
    public void deleteAuthorizationServer(UUID authorizationServerId) {
        var authorizationServer = authorizationServerRepository.findById(authorizationServerId);
        if (authorizationServer.isEmpty()) {
            throw new NotFoundException("An Authorization Server with that id could not be found");
        }
        authorizationServerRepository.delete(authorizationServer.get());
    }

    @Override
    public TemplateInstance renderLogin() {
        return Templates.login();
    }

    private static JWK buildJwk(SigningKey key) throws GeneralSecurityException {
        var publicKey = KeyUtils.decodePublicKey(key.getPublicKey());
        var jwk = new JWK();
        jwk.setKid(key.getId().toString());
        jwk.setAlg(key.getAlgorithm());
        jwk.setE("AQAB");
        jwk.setN(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return jwk;
    }
}
