package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.SingingKeyRepository;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import javax.enterprise.context.ApplicationScoped;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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
    public JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer) {
        try {
            var keys = singingKeyRepository.findAllByAuthorizationServerId(
                    authorizationServer.getId()
            );
            var key =
                    keys.stream()
                            .findFirst()
                            .get();

            var jwk = JWK.parseFromPEMEncodedObjects(key.getPrivateKey());
            var map = jwk.toJSONObject();
            map.put("kid", key.getId().toString());

            return JWK.parse(map);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private SigningKey generateSigningKey(AuthorizationServer authorizationServer) {
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
            singingKeyRepository.save(singingKey);

            return singingKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JWSHeader getJwsHeaderForAuthorizationServer(AuthorizationServer authorizationServer, JWK jwk) {
        return new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(jwk.getKeyID()).build();
    }

    @Override
    public JWKSet getJwksForAuthorizationServer(AuthorizationServer authorizationServer) {
        //generateSigningKey(authorizationServer);

        var singingKeys = singingKeyRepository.findAllByAuthorizationServerId(authorizationServer.getId());
        try {
            List<JWK> keys = new ArrayList<>();
            for (var key : singingKeys) {
                var map = JWK.parseFromPEMEncodedObjects(key.getPublicKey()).toJSONObject();
                map.put("kid", key.getId().toString());
                map.put("alg", "RS256");
                map.put("e", "AQAB");
                keys.add(JWK.parse(map));
            }
            var keySet = new JWKSet(keys);
            return keySet;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
