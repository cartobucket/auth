package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.UserAuthorizationRequest;
import com.cartobucket.auth.routes.mappers.AuthorizationRequestMapper;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.ClientService;
import com.cartobucket.auth.services.UserService;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class AuthorizationServer implements AuthorizationServerApi {
    final AccessTokenService accessTokenService;
    final AuthorizationServerService authorizationServerService;
    final ClientService clientService;
    final UserService userService;

    public AuthorizationServer(
            AccessTokenService accessTokenService,
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserService userService) {
        this.accessTokenService = accessTokenService;
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userService = userService;
    }


    @Override
    @Consumes({"*/*"})
    public Response authorizationServerIdAuthorizationGet(
            UUID authorizationServerId,
            String clientId,
            String responseType,
            String codeChallenge,
            String codeChallengeMethod,
            String redirectUri,
            String scope,
            String state,
            String nonce) {
        return Response.ok().entity(authorizationServerService.renderLogin(authorizationServerId)).build();
    }

    @Override
    public Response authorizationServerIdAuthorizationPost(UUID authorizationServerId, String clientId, String responseType, String username, String password, String codeChallenge, String codeChallengeMethod, String redirectUri, String scope, String state, String nonce) {
        var authorizationRequest = AuthorizationRequestMapper.from(
                clientId,
                responseType,
                codeChallenge,
                codeChallengeMethod,
                redirectUri,
                scope,
                state,
                nonce
        );
        var userAuthorizationRequest = new UserAuthorizationRequest();
        userAuthorizationRequest.setUsername(username);
        userAuthorizationRequest.setPassword(password);
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        final var code = clientService.buildClientCodeForEmailAndPassword(
                authorizationServer,
                authorizationRequest,
                userAuthorizationRequest
        );
        if (code == null) {
            return Response.ok().entity(authorizationServerService.renderLogin(authorizationServerId)).build();
        }
        return Response.status(302).location(
                URI.create(
                        authorizationRequest.getRedirectUri() + "?code=" + code.getCode() + "&state=" + authorizationRequest.getState() + "&nonce=" + authorizationRequest.getNonce() + "&scope" + authorizationRequest.getScope()
                )
        ).build();
    }

    @Override
    public Response authorizationServerIdJwksGet(UUID authorizationServerId) {
        return Response.ok().entity(authorizationServerService.getJwksForAuthorizationServer(
                authorizationServerService.getAuthorizationServer(authorizationServerId))).build();
    }

    @Override
    @Consumes({ "*/*" })
    public Response authorizationServerIdTokenPost(UUID authorizationServerId, AccessTokenRequest accessTokenRequest) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        switch (accessTokenRequest.getGrantType()) {
            case CLIENT_CREDENTIALS -> {
                return Response
                        .ok()
                        .entity(accessTokenService.fromClientCredentials(authorizationServer, accessTokenRequest))
                        .build();
            }
            case AUTHORIZATION_CODE -> {
                return Response
                        .ok()
                        .entity(accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest))
                        .build();
            }
        }
        throw new BadRequestException();
    }

    @Override
    public Response authorizationServerIdUserinfoGet(UUID authorizationServerId, String idToken) {
        final var authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId);
        final var jwks = authorizationServerService.getJwksForAuthorizationServer(authorizationServer);

        try {
            JwtConsumerBuilder builder = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setRequireSubject()
                    .setSkipDefaultAudienceValidation()
                    .setExpectedIssuer(String.valueOf(authorizationServer.getServerUrl()))
                    .setExpectedAudience(authorizationServer.getAudience())
                    .setJwsAlgorithmConstraints(
                            new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                                    AlgorithmIdentifiers.RSA_USING_SHA256));

            JwtClaims jwtClaims = null;
            for (final var jwk : jwks.getKeys()) {
                builder.setVerificationKey(KeyUtils.decodePublicKey(jwk.getN()));
                var jwtConsumer = builder.build();
                jwtClaims = jwtConsumer.processToClaims(idToken.split(" ")[1]);
                if (jwtClaims != null) {
                    break;
                }
            }

            final var userResponse = userService.getUser(UUID.fromString(jwtClaims.getSubject()));
            return Response.ok().entity(userResponse.getProfile()).build();

        } catch (GeneralSecurityException | InvalidJwtException | MalformedClaimException e) {
            throw new RuntimeException(e);
        }
    }

}