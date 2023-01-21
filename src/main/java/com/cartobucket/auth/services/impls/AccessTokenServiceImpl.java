package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.models.*;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.ScopeService;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AccessTokenServiceImpl implements AccessTokenService {
    final ApplicationService applicationService;
    final AuthorizationServerService authorizationServerService;
    final ProfileRepository profileRepository;
    final AuthorizationServerRepository authorizationServerRepository;
    final ClientCodeRepository clientCodeRepository;
    final ClientRepository clientRepository;

    public AccessTokenServiceImpl(
            ApplicationService applicationService,
            AuthorizationServerService authorizationServerService, ProfileRepository profileRepository,
            AuthorizationServerRepository authorizationServerRepository, ClientCodeRepository clientCodeRepository, ClientRepository clientRepository) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
        this.profileRepository = profileRepository;
        this.authorizationServerRepository = authorizationServerRepository;
        this.clientCodeRepository = clientCodeRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public AccessTokenResponse fromClientCredentials(
            AuthorizationServer authorizationServer,
            AccessTokenRequest accessTokenRequest) {
        final var applicationSecret = applicationService.getApplicationSecretFromClientCredentials(
                accessTokenRequest.getClientId(),
                accessTokenRequest.getClientSecret()
        );

        if (applicationSecret == null) {
            throw new BadRequestException("Unable to locate the Application with the credentials provided");
        }

        if (applicationSecret.getAuthorizationServerId() != authorizationServer.getId()) {
            throw new BadRequestException("The Application is not associated with the Authorization Server");
        }

        final var profile = profileRepository.findByResourceAndProfileType(
                applicationSecret.getApplicationId(),
                ProfileType.Application
        );

        var additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("exp", OffsetDateTime.now().plus(authorizationServer.getClientCredentialsTokenExpiration(), ChronoUnit.SECONDS).toEpochSecond());
        // Filter scopes that are associated with the application secret.
        additionalClaims.put(
                "scope",
                ScopeService.scopeListToScopeString(
                        ScopeService.filterScopesByList(
                                accessTokenRequest.getScope(),
                                applicationSecret.getScopes()
                        )
                )
        );

        return buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
    }

    @Override
    public AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        final var clientCode = clientCodeRepository.findByCode(accessTokenRequest.getCode());

        if (clientCode == null || !String.valueOf(clientCode.getClientId()).equals(accessTokenRequest.getClientId())) {
            throw new BadRequestException("Unable to locate the Client with the credentials provided");
        }

        if (clientCode.getAuthorizationServerId() != authorizationServer.getId()) {
            throw new BadRequestException("The Client is not associated with the Authorization Server");
        }

        if (!clientCode.getRedirectUri().equals(accessTokenRequest.getRedirectUri())) {
            throw new BadRequestException("The redirect_uri in the Access Token request does not match the redirect_uri during code generation");
        }

        final var client = clientRepository.findById(clientCode.getClientId()).orElseThrow();
        if (!client.getRedirectUris().contains(URI.create(clientCode.getRedirectUri()))) {
            throw new BadRequestException("The redirect_uri in the Access Token request is not configured for the client");
        }

        if (accessTokenRequest.getCodeVerifier() != null && !isCodeVerified(clientCode, accessTokenRequest.getCodeVerifier())) {
            throw new BadRequestException("Could not verify the PKCE code challenge.");
        }

        final var profile = profileRepository.findByResourceAndProfileType(
                clientCode.getUserId(),
                ProfileType.User
        );

        var additionalClaims = new HashMap<String, Object>();
        if (clientCode.getNonce() != null) {
            additionalClaims.put("nonce", clientCode.getNonce());
        }
        additionalClaims.put("scope", ScopeService.scopeListToScopeString(clientCode.getScopes()));
        additionalClaims.put("exp", OffsetDateTime.now().plus(authorizationServer.getAuthorizationCodeTokenExpiration(), ChronoUnit.SECONDS).toEpochSecond());

        return buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
    }

    private boolean isCodeVerified(ClientCode clientCode, String codeVerifier) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return clientCode
                    .getCodeChallenge()
                    // Base64(SHA-256(codeVerifier))
                    .equals(Base64.getEncoder().encodeToString(messageDigest.digest(codeVerifier.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private AccessTokenResponse buildAccessTokenResponse(
            AuthorizationServer authorizationServer,
            Profile profile,
            Map<String, Object> additionalClaims) {
        var jwk = authorizationServerService.getJwkForAuthorizationServer(authorizationServer);
        try {
            var jwt = Jwt
                    .issuer(authorizationServer.getServerUrl().toExternalForm())
                    .audience(authorizationServer.getAudience())
                    .expiresIn(authorizationServer.getClientCredentialsTokenExpiration());

            jwt.jws()
                    .algorithm(SignatureAlgorithm.valueOf(jwk.getAlg()))
                    .keyId(jwk.getKid());

            for (var entry : additionalClaims.entrySet()) {
                jwt.claim(entry.getKey(), entry.getValue());
            }

            profile.getProfile().forEach(jwt::claim);
            var token = jwt.sign(KeyUtils
                    .decodePrivateKey(authorizationServerService
                            .getSigningKeysForAuthorizationServer(authorizationServer)
                            .getPrivateKey()));

            // TODO: This should be refactored into the caller.
            var accessToken = new AccessTokenResponse();
            accessToken.setAccessToken(token);
            accessToken.setIdToken(token); // TODO: This is obviously wrong.
            accessToken.setTokenType(AccessTokenResponse.TokenTypeEnum.BEARER);
            accessToken.setExpiresIn(Math.toIntExact(authorizationServer.getAuthorizationCodeTokenExpiration()));
            accessToken.setScope((String) additionalClaims.get("scope"));
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
