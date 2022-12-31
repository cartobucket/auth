package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.models.AuthorizationServer;
import com.cartobucket.auth.models.ClientCode;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.repositories.AuthorizationServerRepository;
import com.cartobucket.auth.repositories.ClientCodeRepository;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.ApplicationService;
import com.cartobucket.auth.services.AuthorizationServerService;


import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessTokenServiceImpl implements AccessTokenService {
    final ApplicationService applicationService;
    final AuthorizationServerService authorizationServerService;
    final ProfileRepository profileRepository;

    final AuthorizationServerRepository authorizationServerRepository;
    final ClientCodeRepository clientCodeRepository;

    public AccessTokenServiceImpl(
            ApplicationService applicationService,
            AuthorizationServerService authorizationServerService, ProfileRepository profileRepository,
            AuthorizationServerRepository authorizationServerRepository, ClientCodeRepository clientCodeRepository) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
        this.profileRepository = profileRepository;
        this.authorizationServerRepository = authorizationServerRepository;
        this.clientCodeRepository = clientCodeRepository;
    }

    @Override
    public AccessTokenResponse fromClientCredentials(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        var application = applicationService.getApplicationFromClientCredentials(accessTokenRequest.getClientId(), accessTokenRequest.getClientSecret());
        var profile = profileRepository.findByResourceAndProfileType(application.getId(), ProfileType.Application);
        return buildAccessToken(authorizationServer, profile, null);
    }

    @Override
    public AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        var clientCode = clientCodeRepository.findByCode(accessTokenRequest.getCode());
        var profile = profileRepository.findByResourceAndProfileType(clientCode.getUserId(), ProfileType.User);
        return buildAccessToken(authorizationServer, profile, clientCode);
    }

    @Override
    // TODO: maybe a second method just for client code?
    public AccessTokenResponse buildAccessToken(AuthorizationServer authorizationServer, Profile profile, ClientCode clientCode) {
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
            var token = jwt.sign(authorizationServerService.getSingingKeyForAuthorizationServer(authorizationServer));

            var accessToken = new AccessTokenResponse();
            accessToken.setAccessToken(token);
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
