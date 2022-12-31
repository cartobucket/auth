package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.JWKS;
import com.cartobucket.auth.model.generated.JWKSKeysInner;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.SingingKeyRepository;
import com.cartobucket.auth.services.AuthorizationServerService;

import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;

import java.security.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class AuthorizationServerServiceImpl implements AuthorizationServerService {
    final AuthorizationServerRepository authorizationServerRepository;
    final SingingKeyRepository singingKeyRepository;

    public AuthorizationServerServiceImpl(AuthorizationServerRepository authorizationServerRepository, SingingKeyRepository singingKeyRepository) {
        this.authorizationServerRepository = authorizationServerRepository;
        this.singingKeyRepository = singingKeyRepository;
    }

    @Override
    public AuthorizationServer getDefaultAuthorizationServer() {
        return authorizationServerRepository.findAll().iterator().next();
    }

    @Override
    public JWKSKeysInner getJwkForAuthorizationServer(AuthorizationServer authorizationServer) {
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
            List<JWKSKeysInner> keys = new ArrayList<>();
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

    private static JWKSKeysInner buildJwk(SigningKey key) throws GeneralSecurityException {
        var publicKey = KeyUtils.decodePublicKey(key.getPublicKey());
        var jwk = new JWKSKeysInner();
        jwk.setKid(key.getId().toString());
        jwk.setAlg(key.getAlgorithm());
        jwk.setE("AQAB");
        jwk.setN(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return jwk;
    }
}
