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

import com.cartobucket.auth.rpc.server.entities.mappers.ClientCodeMapper;
import com.cartobucket.auth.rpc.server.entities.mappers.ProfileMapper;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientCodeNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.domain.AccessToken;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.ProfileType;
import com.cartobucket.auth.rpc.server.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.rpc.server.repositories.ClientCodeRepository;
import com.cartobucket.auth.rpc.server.repositories.ClientRepository;
import com.cartobucket.auth.rpc.server.repositories.ProfileRepository;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.data.services.ScopeService;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TokenService implements com.cartobucket.auth.data.services.TokenService {
    final ApplicationService applicationService;
    final AuthorizationServerService authorizationServerService;
    final ProfileRepository profileRepository;
    final AuthorizationServerRepository authorizationServerRepository;
    final ClientCodeRepository clientCodeRepository;
    final ClientRepository clientRepository;

    public TokenService(
            ApplicationService applicationService,
            AuthorizationServerService authorizationServerService,
            ProfileRepository profileRepository,
            AuthorizationServerRepository authorizationServerRepository,
            ClientCodeRepository clientCodeRepository,
            ClientRepository clientRepository) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
        this.profileRepository = profileRepository;
        this.authorizationServerRepository = authorizationServerRepository;
        this.clientCodeRepository = clientCodeRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public AccessToken fromClientCredentials(
            AuthorizationServer authorizationServer,
            String clientId,
            String clientSecret,
            String scopes) throws ProfileNotFound {
        final ApplicationSecret applicationSecret = getApplicationSecretFromClientCredentials(
                clientId,
                clientSecret
        );

        final var profile = profileRepository
                .findByResourceAndProfileType(applicationSecret.getApplicationId(), ProfileType.Application)
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);

        var additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("exp", OffsetDateTime.now().plus(authorizationServer.getClientCredentialsTokenExpiration(), ChronoUnit.SECONDS).toEpochSecond());
        // Filter scopes that are associated with the application secret.
        additionalClaims.put(
                "scope",
                ScopeService.scopeListToScopeString(
                        ScopeService.filterScopesByList(
                                scopes,
                                applicationSecret.getScopes()
                        )
                )
        );

        return buildAccessTokenResponse(authorizationServer, profile, additionalClaims);
    }

    private ApplicationSecret getApplicationSecretFromClientCredentials(String clientId, String clientSecret) {
        // TODO: Copy the code from the previous implementation.
        return new ApplicationSecret();
    }

    @Override
    public AccessToken fromAuthorizationCode(AuthorizationServer authorizationServer, String code, String clientId, String redirectUri, String codeVerifier) throws ClientCodeNotFound, CodeChallengeBadData, ClientNotFound, ProfileNotFound {
        final var clientCode = clientCodeRepository
                .findByCode(code)
                .map(ClientCodeMapper::from)
                .orElseThrow(ClientCodeNotFound::new);

        if (!String.valueOf(clientCode.getClientId()).equals(clientId)) {
            throw new CodeChallengeBadData("Unable to locate the Client with the credentials provided");
        }

        if (clientCode.getAuthorizationServerId() != authorizationServer.getId()) {
            throw new CodeChallengeBadData("The Client is not associated with the Authorization Server");
        }

        if (!clientCode.getRedirectUri().equals(redirectUri)) {
            throw new CodeChallengeBadData("The redirect_uri in the Access Token request does not match the redirect_uri during code generation");
        }

        final var client = clientRepository
                .findById(clientCode.getClientId())
                .orElseThrow(ClientNotFound::new);
        if (!client.getRedirectUris().contains(URI.create(clientCode.getRedirectUri()))) {
            throw new CodeChallengeBadData("The redirect_uri in the Access Token request is not configured for the client");
        }

        if (codeVerifier != null && !isCodeVerified(clientCode, codeVerifier)) {
            throw new CodeChallengeBadData("Could not verify the PKCE code challenge.");
        }

        final var profile = profileRepository.findByResourceAndProfileType(
                clientCode.getUserId(),
                ProfileType.User
                )
                .map(ProfileMapper::from)
                .orElseThrow(ProfileNotFound::new);

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

    private AccessToken buildAccessTokenResponse(
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
