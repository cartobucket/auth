package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ClientRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
    public AccessTokenResponse fromClientCredentials(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        final var application = applicationService.getApplicationFromClientCredentials(
                accessTokenRequest.getClientId(),
                accessTokenRequest.getClientSecret()
        );
        if (application == null) {
            throw new BadRequestException("Unable to locate the Application with the credentials provided");
        }
        final var profile = profileRepository.findByResourceAndProfileType(application.getId(), ProfileType.Application);
        return buildAccessToken(authorizationServer, profile, null);
    }

    @Override
    public AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        final var clientCode = clientCodeRepository.findByCode(accessTokenRequest.getCode());
        if (clientCode == null || !String.valueOf(clientCode.getClientId()).equals(accessTokenRequest.getClientId())) {
            throw new BadRequestException("Unable to locate the Client with the credentials provided");
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

        final var profile = profileRepository.findByResourceAndProfileType(clientCode.getUserId(), ProfileType.User);
        return buildAccessToken(authorizationServer, profile, clientCode);
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

    // TODO: maybe a second method just for client code?
    private AccessTokenResponse buildAccessToken(AuthorizationServer authorizationServer, Profile profile, ClientCode clientCode) {
        var jwk = authorizationServerService.getJwkForAuthorizationServer(authorizationServer);
        try {
            var jwt = Jwt
                    .issuer(authorizationServer.getServerUrl().toExternalForm())
                    .audience(authorizationServer.getAudience())
                    .expiresIn(authorizationServer.getClientCredentialsTokenExpiration());

            jwt.jws()
                    .algorithm(SignatureAlgorithm.valueOf(jwk.getAlg()))
                    .keyId(jwk.getKid());

            if (clientCode != null && clientCode.getNonce() != null) {
                jwt.claim("nonce", clientCode.getNonce());
            }
            profile.getProfile().forEach(jwt::claim);
            var token = jwt.sign(KeyUtils
                    .decodePrivateKey(authorizationServerService
                            .getSigningKeysForAuthorizationServer(authorizationServer)
                            .getPrivateKey()));

            var accessToken = new AccessTokenResponse();
            accessToken.setAccessToken(token);
            accessToken.setIdToken(token); // TODO: This is obviously wrong.
            accessToken.setTokenType(AccessTokenResponse.TokenTypeEnum.BEARER);
            accessToken.setExpiresIn(
                    Math.toIntExact(
                            clientCode != null ? authorizationServer.getAuthorizationCodeTokenExpiration() :
                                    authorizationServer.getClientCredentialsTokenExpiration()
                    )
            );
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
