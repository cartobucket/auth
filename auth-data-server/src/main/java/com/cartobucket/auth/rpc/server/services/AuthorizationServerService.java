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

import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.JWK;
import com.cartobucket.auth.data.domain.JWKS;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.rpc.server.entities.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.SigningKeyMapper;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.rpc.server.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.rpc.server.repositories.ScopeRepository;
import com.cartobucket.auth.rpc.server.repositories.SingingKeyRepository;
import com.cartobucket.auth.data.services.TemplateService;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class AuthorizationServerService implements com.cartobucket.auth.data.services.AuthorizationServerService {
    final AuthorizationServerRepository authorizationServerRepository;
    final SingingKeyRepository singingKeyRepository;
    final TemplateService templateService;
    final ScopeRepository scopeRepository;

    public AuthorizationServerService(AuthorizationServerRepository authorizationServerRepository, SingingKeyRepository singingKeyRepository, TemplateService templateService, ScopeRepository scopeRepository) {
        this.authorizationServerRepository = authorizationServerRepository;
        this.singingKeyRepository = singingKeyRepository;
        this.templateService = templateService;
        this.scopeRepository = scopeRepository;
    }

    @Override
    public JWK getJwkForAuthorizationServer(AuthorizationServer authorizationServer) {
        try {
            return buildJwk(getSigningKeysForAuthorizationServer(authorizationServer));
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
    public JWKS getJwksForAuthorizationServer(UUID authorizationServerId) {
        var singingKeys = singingKeyRepository.findAllByAuthorizationServerId(authorizationServerId);
        try {
            List<JWK> keys = new ArrayList<>();
            for (var key : singingKeys) {
                keys.add(buildJwk(SigningKeyMapper.from(key)));
            }
            var jwks = new JWKS();
            jwks.setKeys(keys);
            return jwks;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public AuthorizationServer createAuthorizationServer(AuthorizationServer authorizationServer) {
        authorizationServer.setCreatedOn(OffsetDateTime.now());
        authorizationServer.setUpdatedOn(OffsetDateTime.now());

        authorizationServer = AuthorizationServerMapper.from(authorizationServerRepository.save(AuthorizationServerMapper.to(authorizationServer)));

        // Create the signing keys
        var signingKey = SigningKeyMapper.to(generateSigningKey(authorizationServer));
        singingKeyRepository.save(signingKey);

        // Create the templates
        createDefaultTemplatesForAuthorizationServer(authorizationServer);

        return authorizationServer;
    }

    @Override
    public AuthorizationServer getAuthorizationServer(UUID authorizationServerId) throws AuthorizationServerNotFound {
        return authorizationServerRepository
                .findById(authorizationServerId)
                .map(AuthorizationServerMapper::from)
                .orElseThrow(AuthorizationServerNotFound::new);
    }

    @Override
    @Transactional
    public AuthorizationServer updateAuthorizationServer(final UUID authorizationServerId, final AuthorizationServer authorizationServer) throws AuthorizationServerNotFound {
        var _authorizationServer = authorizationServerRepository
                .findById(authorizationServerId)
                .orElseThrow(AuthorizationServerNotFound::new);

        _authorizationServer.setCreatedOn(authorizationServer.getCreatedOn());
        _authorizationServer.setUpdatedOn(OffsetDateTime.now());
        _authorizationServer.setServerUrl(authorizationServer.getServerUrl());
        _authorizationServer.setAudience(authorizationServer.getAudience());
        _authorizationServer.setName(authorizationServer.getName());
        _authorizationServer.setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration());
        _authorizationServer.setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration());
        return AuthorizationServerMapper.from(authorizationServerRepository.save(_authorizationServer));
    }

    @Override
    public List<AuthorizationServer> getAuthorizationServers() {
        return StreamSupport
                .stream(authorizationServerRepository.findAll().spliterator(), false)
                .map(AuthorizationServerMapper::from)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAuthorizationServer(final UUID authorizationServerId) throws AuthorizationServerNotFound {
        authorizationServerRepository.delete(
                authorizationServerRepository
                        .findById(authorizationServerId)
                        .orElseThrow(AuthorizationServerNotFound::new)
        );
        // TODO this should cascade.
    }

    @Override
    public SigningKey getSigningKeysForAuthorizationServer(AuthorizationServer authorizationServer) {
        var keys = singingKeyRepository.findAllByAuthorizationServerId(
                authorizationServer.getId()
        );
        var key =
                keys.stream()
                        .findFirst()
                        .map(SigningKeyMapper::from)
                        .orElseThrow();
        return key;
    }

    @Override
    public JwtClaims validateJwtForAuthorizationServer(AuthorizationServer authorizationServer, String Jwt) throws NotAuthorized {
        final var jwks = getJwksForAuthorizationServer(authorizationServer.getId());

        try {
            JwtConsumerBuilder builder = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireSubject()
                    .setSkipDefaultAudienceValidation()
                    .setExpectedIssuer(String.valueOf(authorizationServer.getServerUrl()))
                    .setExpectedAudience(authorizationServer.getAudience())
                    .setJwsAlgorithmConstraints(
                            new org.jose4j.jwa.AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                                    AlgorithmIdentifiers.RSA_USING_SHA256));

            JwtClaims jwtClaims = null;
            for (final var jwk : jwks.getKeys()) {
                var singingKey = singingKeyRepository.findByIdAndAuthorizationServerId(
                        UUID.fromString(jwk.getKid()),
                        authorizationServer.getId()
                );
                builder.setVerificationKey(KeyUtils.decodePublicKey(singingKey.getPublicKey()));
                var jwtConsumer = builder.build();
                jwtClaims = jwtConsumer.processToClaims(Jwt.split(" ")[1]);
                if (jwtClaims != null) {
                    return jwtClaims;
                }
            }
        } catch (InvalidJwtException e) {
            throw new NotAuthorized(e.getMessage());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static JWK buildJwk(SigningKey key) throws GeneralSecurityException {
        var publicKey = KeyUtils.decodePublicKey(key.getPublicKey());
        var jwk = new JWK();
        jwk.setKid(key.getId().toString());
        jwk.setKty("RSA");
        jwk.setUse("sig");
        jwk.setAlg(key.getAlgorithm());
        jwk.setE("AQAB");
        jwk.setN(Base64.getUrlEncoder().encodeToString(((RSAPublicKey)publicKey).getModulus().toByteArray()));
        jwk.setE(Base64.getUrlEncoder().encodeToString(((RSAPublicKey)publicKey).getPublicExponent().toByteArray()));
        return jwk;
    }

    private void createDefaultTemplatesForAuthorizationServer(AuthorizationServer authorizationServer) {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/login.html")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                var template = new Template();
                template.setTemplate(Base64.getEncoder().encode(contents.getBytes()));
                template.setTemplateType(TemplateTypeEnum.LOGIN);
                template.setAuthorizationServerId(authorizationServer.getId());
                templateService.createTemplate(template);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}