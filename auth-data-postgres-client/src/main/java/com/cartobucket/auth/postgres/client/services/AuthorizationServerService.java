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

package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.JWK;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.domain.SigningKey;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.domain.TemplateTypeEnum;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.data.services.TemplateService;
import com.cartobucket.auth.postgres.client.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.SingingKeyRepository;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.postgres.client.entities.mappers.ProfileMapper;
import com.cartobucket.auth.postgres.client.entities.mappers.SigningKeyMapper;
import com.cartobucket.auth.postgres.client.repositories.ProfileRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
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
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthorizationServerService implements com.cartobucket.auth.data.services.AuthorizationServerService {
    final AuthorizationServerRepository authorizationServerRepository;
    final EventRepository eventRepository;
    final ProfileRepository profileRepository;
    final SingingKeyRepository singingKeyRepository;
    final TemplateService templateService;

    public AuthorizationServerService(
            AuthorizationServerRepository authorizationServerRepository,
            EventRepository eventRepository,
            ProfileRepository profileRepository,
            SingingKeyRepository singingKeyRepository,
            TemplateService templateService
    ) {
        this.authorizationServerRepository = authorizationServerRepository;
        this.eventRepository = eventRepository;
        this.profileRepository = profileRepository;
        this.singingKeyRepository = singingKeyRepository;
        this.templateService = templateService;
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
    @Transactional
    public List<JWK> getJwksForAuthorizationServer(UUID authorizationServerId) {
        return singingKeyRepository
                .findAllByAuthorizationServerId(
                        authorizationServerId
                )
                .stream()
                .map(AuthorizationServerService::buildJwk)
                .toList();
    }

    @Override
    @Transactional
    public AccessToken generateAccessToken(
            UUID authorizationServerId,
            UUID userId,
            String subject,
            List<Scope> scopes,
            long expiresInSeconds,
            String nonce) {
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow();

        final var profile = profileRepository.findByResourceId(
                        userId
                )
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);

        var additionalClaims = new HashMap<String, Object>();
        if (nonce != null) {
            additionalClaims.put("nonce", nonce);
        }
        additionalClaims.put("sub", subject);
        additionalClaims.put("scope", ScopeService.scopeListToScopeString(scopes.stream().map(Scope::getName).toList()));
        additionalClaims.put("exp", OffsetDateTime.now().plusSeconds(expiresInSeconds).toEpochSecond());

        return buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
    }

    @Override
    @Transactional
    public AuthorizationServer createAuthorizationServer(AuthorizationServer authorizationServer) {
        authorizationServer.setCreatedOn(OffsetDateTime.now());
        authorizationServer.setUpdatedOn(OffsetDateTime.now());

        var _authoriztionServer = AuthorizationServerMapper.to(authorizationServer);
        authorizationServerRepository.persist(_authoriztionServer);
        authorizationServer = AuthorizationServerMapper.from(_authoriztionServer);
        eventRepository.createAuthorizationServerEvent(
                authorizationServer,
                EventType.CREATE
        );

        // Create the signing keys
        var signingKey = SigningKeyMapper.to(generateSigningKey(authorizationServer));
        singingKeyRepository.persist(signingKey);
        eventRepository.createSingingKeyEvent(
                SigningKeyMapper.from(signingKey),
                EventType.CREATE
        );

        // Create the templates
        createDefaultTemplatesForAuthorizationServer(authorizationServer);

        return authorizationServer;
    }

    @Override
    @Transactional
    public AuthorizationServer getAuthorizationServer(UUID authorizationServerId) throws AuthorizationServerNotFound {
        return authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .map(AuthorizationServerMapper::from)
                .orElseThrow(AuthorizationServerNotFound::new);
    }

    @Override
    @Transactional
    public AuthorizationServer updateAuthorizationServer(final UUID authorizationServerId, final AuthorizationServer authorizationServer) throws AuthorizationServerNotFound {
        var _authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow(AuthorizationServerNotFound::new);

        _authorizationServer.setCreatedOn(authorizationServer.getCreatedOn());
        _authorizationServer.setUpdatedOn(OffsetDateTime.now());
        _authorizationServer.setServerUrl(authorizationServer.getServerUrl());
        _authorizationServer.setAudience(authorizationServer.getAudience());
        _authorizationServer.setName(authorizationServer.getName());
        _authorizationServer.setAuthorizationCodeTokenExpiration(authorizationServer.getAuthorizationCodeTokenExpiration());
        _authorizationServer.setClientCredentialsTokenExpiration(authorizationServer.getClientCredentialsTokenExpiration());
        authorizationServerRepository.persist(_authorizationServer);
        eventRepository.createAuthorizationServerEvent(
                AuthorizationServerMapper.from(_authorizationServer),
                EventType.UPDATE
        );
        return AuthorizationServerMapper.from(_authorizationServer);
    }

    @Override
    @Transactional
    public List<AuthorizationServer> getAuthorizationServers(Page page) {
        return authorizationServerRepository
                .findAll(Sort.descending("createdOn"))
                .range(page.offset(), page.getNextRowsCount())
                .list()
                .stream()
                .map(AuthorizationServerMapper::from)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAuthorizationServer(final UUID authorizationServerId) throws AuthorizationServerNotFound {
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow(AuthorizationServerNotFound::new);
        authorizationServerRepository.delete(authorizationServer);
        eventRepository.createAuthorizationServerEvent(
                AuthorizationServerMapper.from(authorizationServer),
                EventType.DELETE
        );
    }

    @Override
    public SigningKey getSigningKeysForAuthorizationServer(final UUID authorizationServerId) {
        var keys = singingKeyRepository.findAllByAuthorizationServerId(
                authorizationServerId
        );
        var key =
                keys.stream()
                        .skip(new Random().nextInt(keys.size()))
                        .findFirst()
                        .map(SigningKeyMapper::from)
                        .orElseThrow();
        return key;
    }

    @Override
    @Transactional
    public Map<String, Object> validateJwtForAuthorizationServer(UUID authorizationServerId, String Jwt) throws NotAuthorized {
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow();
        final var jwks = getJwksForAuthorizationServer(authorizationServer.getId());

        try {
            JwtConsumerBuilder builder = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireSubject()
                    .setSkipDefaultAudienceValidation()
                    .setExpectedIssuer(authorizationServer.getServerUrl().toString() + authorizationServer.getId() + "/")
                    .setExpectedAudience(authorizationServer.getAudience())
                    .setJwsAlgorithmConstraints(
                            new org.jose4j.jwa.AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                                    AlgorithmIdentifiers.RSA_USING_SHA256));

            JwtClaims jwtClaims;
            for (final var jwk : jwks) {
                var singingKey = singingKeyRepository.findByIdAndAuthorizationServerId(
                        UUID.fromString(jwk.getKid()),
                        authorizationServer.getId()
                );
                builder.setVerificationKey(KeyUtils.decodePublicKey(singingKey.getPublicKey()));
                var jwtConsumer = builder.build();
                jwtClaims = jwtConsumer.processToClaims(Jwt.split(" ")[1]);
                if (jwtClaims != null) {
                    return jwtClaims.getClaimsMap();
                }
            }
        } catch (InvalidJwtException e) {
            throw new NotAuthorized(e.getMessage());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static JWK buildJwk(com.cartobucket.auth.postgres.client.entities.SigningKey key) {
        var jwk = new JWK();

        try {
            var publicKey = KeyUtils.decodePublicKey(key.getPublicKey());
            // Some clients will not accept padding in the base64 encoded string.
            jwk.setN(Base64.getUrlEncoder().withoutPadding().encodeToString(((RSAPublicKey)publicKey).getModulus().toByteArray()));
            jwk.setE(Base64.getUrlEncoder().withoutPadding().encodeToString(((RSAPublicKey)publicKey).getPublicExponent().toByteArray()));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        jwk.setKid(key.getId().toString());
        jwk.setKty("RSA");
        jwk.setUse("sig");
        jwk.setAlg(
            switch (key.getKeyType()) {
                default -> "RS256";
            }
        );
        jwk.setE("AQAB");
        jwk.setX5c(Collections.emptyList());
        jwk.setX5t("");
        jwk.setX5tHashS256("");
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
                var _template = templateService.createTemplate(template);
                eventRepository.createTemplateEvent(
                        _template,
                        EventType.CREATE
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AccessToken buildAccessTokenResponse(
            com.cartobucket.auth.postgres.client.entities.AuthorizationServer authorizationServer,
            Profile profile,
            Map<String, Object> additionalClaims) {
        final var signingKey = getSigningKeysForAuthorizationServer(authorizationServer.getId());
        try {
            var jwt = Jwt
                    .issuer(authorizationServer.getServerUrl().toExternalForm() + authorizationServer.getId() + "/")
                    .audience(authorizationServer.getAudience())
                    .expiresIn(authorizationServer.getClientCredentialsTokenExpiration());

            jwt.jws()
                    .algorithm(
                            switch (signingKey.getKeyType()) {
                                default ->
                                        SignatureAlgorithm.RS256;
                            }
                    )
                    .keyId(String.valueOf(signingKey.getId()));

            additionalClaims.forEach(jwt::claim);

            profile.getProfile().forEach(jwt::claim);
            final var token = jwt.sign(KeyUtils.decodePrivateKey(signingKey.getPrivateKey()));

            // TODO: This should be refactored into the caller.
            var accessToken = new AccessToken();
            accessToken.setAccessToken(token);
            accessToken.setIdToken(token); // TODO: This is obviously wrong.
            accessToken.setTokenType(AccessToken.TokenTypeEnum.BEARER);
            accessToken.setExpiresIn(Math.toIntExact(authorizationServer.getAuthorizationCodeTokenExpiration()));
            accessToken.setScope((String) additionalClaims.get("scope"));
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
