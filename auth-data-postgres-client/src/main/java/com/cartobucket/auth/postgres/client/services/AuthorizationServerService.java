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

import com.cartobucket.auth.data.domain.*;
import com.cartobucket.auth.data.exceptions.NotAuthorized;
import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.data.services.SchemaService;
import com.cartobucket.auth.data.services.TemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cartobucket.auth.postgres.client.repositories.ApplicationSecretRepository;
import com.cartobucket.auth.postgres.client.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.RefreshTokenRepository;
import com.cartobucket.auth.postgres.client.repositories.ScopeRepository;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
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
    final ApplicationSecretRepository applicationSecretRepository;
    final EventRepository eventRepository;
    final ProfileRepository profileRepository;
    final RefreshTokenRepository refreshTokenRepository;
    final ScopeRepository scopeRepository;
    final SingingKeyRepository singingKeyRepository;
    final TemplateService templateService;
    final ScopeService scopeService;
    final SchemaService schemaService;

    public AuthorizationServerService(
            AuthorizationServerRepository authorizationServerRepository,
            ApplicationSecretRepository applicationSecretRepository,
            EventRepository eventRepository,
            ProfileRepository profileRepository,
            RefreshTokenRepository refreshTokenRepository,
            ScopeRepository scopeRepository,
            SingingKeyRepository singingKeyRepository,
            TemplateService templateService,
            ScopeService scopeService,
            SchemaService schemaService
    ) {
        this.authorizationServerRepository = authorizationServerRepository;
        this.applicationSecretRepository = applicationSecretRepository;
        this.eventRepository = eventRepository;
        this.profileRepository = profileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.scopeRepository = scopeRepository;
        this.singingKeyRepository = singingKeyRepository;
        this.templateService = templateService;
        this.scopeService = scopeService;
        this.schemaService = schemaService;
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
    public AccessToken generateClientCredentialsAccessToken(
            UUID authorizationServerId,
            UUID applicationId,
            String subject,
            List<Scope> scopes,
            long expiresInSeconds) {
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow();

        final var profile = profileRepository.findByResourceId(applicationId)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);

        var additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("sub", subject);
        if (scopes != null && !scopes.isEmpty()) {
            additionalClaims.put("scope", ScopeService.scopeListToScopeString(scopes.stream().map(Scope::getName).toList()));
        }
        additionalClaims.put("exp", OffsetDateTime.now().plusSeconds(expiresInSeconds).toEpochSecond());

        return buildClientCredentialsAccessToken(authorizationServer, profile, additionalClaims);
    }

    @Override
    @Transactional
    public AccessToken generateAuthorizationCodeFlowAccessToken(
            UUID authorizationServerId,
            UUID userId,
            String subject,
            String clientId,
            List<Scope> scopes,
            long expiresInSeconds,
            String nonce) {
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow();

        final var profile = profileRepository.findByResourceId(userId)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);

        var additionalClaims = new HashMap<String, Object>();
        if (nonce != null) {
            additionalClaims.put("nonce", nonce);
        }
        additionalClaims.put("sub", subject);
        if (scopes != null && !scopes.isEmpty()) {
            additionalClaims.put("scope", ScopeService.scopeListToScopeString(scopes.stream().map(Scope::getName).toList()));
        } else {
            additionalClaims.put("scope", "openid");
        }
        additionalClaims.put("exp", OffsetDateTime.now().plusSeconds(expiresInSeconds).toEpochSecond());

        var accessToken = buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
        
        // Generate refresh token
        var refreshToken = generateRefreshToken();
        var refreshTokenHash = hashRefreshToken(refreshToken);
        
        // Store refresh token in database
        var refreshTokenEntity = new com.cartobucket.auth.postgres.client.entities.RefreshToken();
        refreshTokenEntity.tokenHash = refreshTokenHash;
        refreshTokenEntity.userId = userId;
        refreshTokenEntity.clientId = clientId;
        refreshTokenEntity.authorizationServerId = authorizationServerId;
        refreshTokenEntity.scopeIds = scopes != null ? scopes.stream().map(Scope::getId).toList() : Collections.emptyList();
        refreshTokenEntity.expiresAt = OffsetDateTime.now().plusDays(30);
        refreshTokenEntity.createdOn = OffsetDateTime.now();
        refreshTokenEntity.isRevoked = false;
        
        refreshTokenRepository.persist(refreshTokenEntity);
        accessToken.setRefreshToken(refreshToken);
        
        return accessToken;
    }
    

    @Override
    @Transactional
    public AccessToken refreshAccessToken(UUID authorizationServerId, String refreshToken, String clientId) {
        // Hash the incoming refresh token to match against stored hash
        var tokenHash = hashRefreshToken(refreshToken);
        
        // Find the refresh token in the database
        var storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new NotAuthorized("Invalid or expired refresh token"));
        
        // Verify the client ID matches
        if (!storedToken.clientId.equals(clientId)) {
            throw new NotAuthorized("Client ID mismatch");
        }
        
        // Verify the authorization server ID matches
        if (!storedToken.authorizationServerId.equals(authorizationServerId)) {
            throw new NotAuthorized("Authorization server mismatch");
        }
        
        // Immediately revoke the old refresh token (token rotation)
        storedToken.isRevoked = true;
        storedToken.revokedAt = OffsetDateTime.now();
        refreshTokenRepository.persist(storedToken);
        
        // Get the user profile
        final var profile = profileRepository.findByResourceId(storedToken.userId)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);
        
        // Get the authorization server
        final var authorizationServer = authorizationServerRepository
                .findByIdOptional(authorizationServerId)
                .orElseThrow();
        
        // Get scopes from stored scope IDs
        var scopes = storedToken.scopeIds.stream()
                .map(scopeId -> {
                    try {
                        return com.cartobucket.auth.postgres.client.entities.mappers.ScopeMapper.from(
                            scopeRepository.findByIdOptional(scopeId)
                                .orElseThrow(() -> new RuntimeException("Scope not found: " + scopeId))
                        );
                    } catch (Exception e) {
                        // If scope is deleted, create a basic scope with the ID for backwards compatibility
                        var scope = new Scope();
                        scope.setId(scopeId);
                        return scope;
                    }
                })
                .toList();
        
        // Generate new access token
        var additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("sub", storedToken.clientId);
        additionalClaims.put("scope", ScopeService.scopeListToScopeString(scopes.stream().map(Scope::getName).toList()));
        additionalClaims.put("exp", OffsetDateTime.now().plusSeconds(authorizationServer.getAuthorizationCodeTokenExpiration()).toEpochSecond());
        
        var newAccessToken = buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
        
        // Generate and store a new refresh token (token rotation)
        var newRefreshToken = generateRefreshToken();
        var newRefreshTokenHash = hashRefreshToken(newRefreshToken);
        
        var newRefreshTokenEntity = new com.cartobucket.auth.postgres.client.entities.RefreshToken();
        newRefreshTokenEntity.tokenHash = newRefreshTokenHash;
        newRefreshTokenEntity.userId = storedToken.userId;
        newRefreshTokenEntity.clientId = storedToken.clientId;
        newRefreshTokenEntity.authorizationServerId = storedToken.authorizationServerId;
        newRefreshTokenEntity.scopeIds = storedToken.scopeIds;
        newRefreshTokenEntity.expiresAt = OffsetDateTime.now().plusDays(30); // 30 days expiration
        newRefreshTokenEntity.createdOn = OffsetDateTime.now();
        newRefreshTokenEntity.isRevoked = false;
        
        refreshTokenRepository.persist(newRefreshTokenEntity);
        newAccessToken.setRefreshToken(newRefreshToken);
        
        return newAccessToken;
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

        // Create the default scopes
        createDefaultScopesForAuthorizationServer(authorizationServer);

        // Create the templates
        createDefaultTemplatesForAuthorizationServer(authorizationServer);

        // Create the default OIDC schemas
        createDefaultSchemasForAuthorizationServer(authorizationServer);

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

    private String generateRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    private String hashRefreshToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    @Transactional
    public void cleanupExpiredRefreshTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }
    
    @Transactional  
    public void revokeAllUserRefreshTokens(UUID userId) {
        refreshTokenRepository.revokeAllUserTokens(userId);
    }
    
    private void createDefaultScopesForAuthorizationServer(AuthorizationServer authorizationServer) {
        String[] defaultScopeNames = {"openid", "email", "profile", "offline_access"};
        
        for (String scopeName : defaultScopeNames) {
            var scope = new Scope();
            scope.setName(scopeName);
            scope.setAuthorizationServer(authorizationServer);
            var metadata = new Metadata();
            metadata.setIdentifiers(Collections.emptyList());
            metadata.setProperties(Collections.emptyMap());
            scope.setMetadata(metadata);
            scopeService.createScope(scope);
        }
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
                var metadata = new Metadata();
                metadata.setIdentifiers(Collections.emptyList());
                metadata.setProperties(Collections.emptyMap());
                template.setMetadata(metadata);
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

    private void createDefaultSchemasForAuthorizationServer(AuthorizationServer authorizationServer) {
        try (InputStream inputStream = getClass().getResourceAsStream("/schemas/oidc-userinfo-claims.json")) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                    var objectMapper = new ObjectMapper();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> schemaMap = objectMapper.readValue(contents, Map.class);
                    
                    var schema = new Schema();
                    schema.setName("oidc-userinfo-claims");
                    schema.setAuthorizationServerId(authorizationServer.getId());
                    schema.setJsonSchemaVersion("https://json-schema.org/draft/2020-12/schema");
                    schema.setSchema(schemaMap);
                    schema.setCreatedOn(OffsetDateTime.now());
                    schema.setUpdatedOn(OffsetDateTime.now());
                    
                    var metadata = new Metadata();
                    metadata.setIdentifiers(Collections.emptyList());
                    metadata.setProperties(Collections.emptyMap());
                    schema.setMetadata(metadata);
                    
                    schemaService.createSchema(schema);
                }
            }
        } catch (IOException e) {
            // Log warning but don't fail authorization server creation if schema creation fails
            System.err.println("Warning: Could not create OIDC UserInfo claims schema: " + e.getMessage());
        }
    }

    private AccessToken buildAccessTokenResponse(
            com.cartobucket.auth.postgres.client.entities.AuthorizationServer authorizationServer,
            Profile profile,
            Map<String, Object> additionalClaims) {
        final var signingKey = getSigningKeysForAuthorizationServer(authorizationServer.getId());
        try {
            var jwt = Jwt
                    .issuer(authorizationServer.getServerUrl().toExternalForm() + "/" + authorizationServer.getId() + "/")
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

            var accessToken = new AccessToken();
            accessToken.setAccessToken(token);
            accessToken.setIdToken(token); // TODO: ID token should be separate from access token
            accessToken.setTokenType(TokenTypeEnum.BEARER);
            accessToken.setExpiresIn(Math.toIntExact(authorizationServer.getAuthorizationCodeTokenExpiration()));
            accessToken.setScope((String) additionalClaims.get("scope"));
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private AccessToken buildClientCredentialsAccessToken(
            com.cartobucket.auth.postgres.client.entities.AuthorizationServer authorizationServer,
            Profile profile,
            Map<String, Object> additionalClaims) {
        final var signingKey = getSigningKeysForAuthorizationServer(authorizationServer.getId());
        try {
            var jwt = Jwt
                    .issuer(authorizationServer.getServerUrl().toExternalForm() + "/" + authorizationServer.getId() + "/")
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

            var accessToken = new AccessToken();
            accessToken.setAccessToken(token);
            // No ID token for client credentials flow
            accessToken.setTokenType(TokenTypeEnum.BEARER);
            accessToken.setExpiresIn(Math.toIntExact(authorizationServer.getClientCredentialsTokenExpiration()));
            accessToken.setScope((String) additionalClaims.get("scope"));
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
