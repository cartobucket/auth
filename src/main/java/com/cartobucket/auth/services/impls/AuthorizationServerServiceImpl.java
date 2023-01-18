package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.*;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.SigningKey;
import com.cartobucket.auth.models.mappers.AuthorizationServerMapper;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.ScopeRepository;
import com.cartobucket.auth.repositories.SingingKeyRepository;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.TemplateService;
import io.quarkus.qute.Qute;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class AuthorizationServerServiceImpl implements AuthorizationServerService {
    final AuthorizationServerRepository authorizationServerRepository;
    final SingingKeyRepository singingKeyRepository;
    final TemplateService templateService;
    final ScopeRepository scopeRepository;

    public AuthorizationServerServiceImpl(AuthorizationServerRepository authorizationServerRepository, SingingKeyRepository singingKeyRepository, TemplateService templateService, ScopeRepository scopeRepository) {
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
    public String renderLogin(UUID authorizationServerId) {
        var template = templateService.getTemplateForAuthorizationServer(authorizationServerId);
        return Qute.fmt(new String(Base64.getDecoder().decode(template.getTemplate()))).render();
    }

    @Override
    public SigningKey getSigningKeysForAuthorizationServer(AuthorizationServer authorizationServer) {
        var keys = singingKeyRepository.findAllByAuthorizationServerId(
                authorizationServer.getId()
        );
        var key =
                keys.stream()
                        .findFirst()
                        .orElseThrow();
        return key;
    }

    @Override
    public JwtClaims validateJwtForAuthorizationServer(AuthorizationServer authorizationServer, String Jwt) {
        final var jwks = getJwksForAuthorizationServer(authorizationServer);

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
        } catch (InvalidJwtException | GeneralSecurityException e) {
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
}
