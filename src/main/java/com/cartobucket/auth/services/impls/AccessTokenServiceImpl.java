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
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import java.text.ParseException;
import java.time.Instant;

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
        var accessToken = buildAccessToken(authorizationServer, profile, null);

        var accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccessToken(accessToken.serialize());
        accessTokenResponse.setIdToken(null);
        accessTokenResponse.setRefreshToken(null);
        accessTokenResponse.setTokenType(AccessTokenResponse.TokenTypeEnum.BEARER);
        accessTokenResponse.setExpiresIn(Math.toIntExact(authorizationServer.getClientCredentialsTokenExpiration()));
        return accessTokenResponse;
    }

    @Override
    public AccessTokenResponse fromAuthorizationCode(AuthorizationServer authorizationServer, AccessTokenRequest accessTokenRequest) {
        var clientCode = clientCodeRepository.findByCode(accessTokenRequest.getCode());
        var profile = profileRepository.findByResourceAndProfileType(clientCode.getUserId(), ProfileType.User);
        var accessToken = buildAccessToken(authorizationServer, profile, clientCode);

        var accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccessToken(accessToken.serialize());
        accessTokenResponse.setIdToken(accessToken.serialize());
        accessTokenResponse.setRefreshToken(null);
        accessTokenResponse.setTokenType(AccessTokenResponse.TokenTypeEnum.BEARER);
        accessTokenResponse.setExpiresIn(Math.toIntExact(authorizationServer.getClientCredentialsTokenExpiration()));
        return accessTokenResponse;
    }

    @Override
    // TODO: maybe a second method just for client code?
    public SignedJWT buildAccessToken(AuthorizationServer authorizationServer, Profile profile, ClientCode clientCode) {
        JWK jwk = authorizationServerService.getJwkForAuthorizationServer(authorizationServer);
        try {
            // Prepare JWT with claims set
            var _profile = profile.getProfile();
            _profile.put("iss", authorizationServer.getServerUrl().toString());
            _profile.put("aud", authorizationServer.getAudience());
            _profile.put("exp", (Instant.now().toEpochMilli() / 1000) + authorizationServer.getClientCredentialsTokenExpiration());
            if (clientCode != null) {
                _profile.put("nonce", clientCode.getNonce());
            }
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(_profile);

            SignedJWT signedJWT = new SignedJWT(
                    authorizationServerService.getJwsHeaderForAuthorizationServer(authorizationServer, jwk),
                    claimsSet
            );

            // Compute the RSA signature
            signedJWT.sign(new RSASSASigner((RSAKey) jwk));
            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
