package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.model.generated.JWKS;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.ClientService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.jwt.util.KeyUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

@Path("/authorizationServer/")
public class AuthorizationServer implements AuthorizationServerApi {
    final AccessTokenService accessTokenService;

    final AuthorizationServerService authorizationServerService;

    final ClientService clientService;

    final UserRepository userRepository;

    final ProfileRepository profileRepository;

    public AuthorizationServer(
            AccessTokenService accessTokenService,
            AuthorizationServerService authorizationServerService,
            ClientService clientService,
            UserRepository userRepository,
            ProfileRepository profileRepository
    ) {
        this.accessTokenService = accessTokenService;
        this.authorizationServerService = authorizationServerService;
        this.clientService = clientService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }


    @Override
    public String authorizationServerIdAuthorizationGet(UUID authorizationServerId, String responseType, String clientId, String redirectUri, String scope, String state) {
        return Templates.login().render();
    }

    @Override
    public JWKS authorizationServerIdJwksGet(UUID authorizationServerId) {
        return authorizationServerService.getJwksForAuthorizationServer(
                authorizationServerService.getDefaultAuthorizationServer()
        );
    }

    @Override
    @Consumes({ "*/*" })
    public AccessTokenResponse authorizationServerIdTokenPost(UUID authorizationServerId, AccessTokenRequest accessTokenRequest) {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
        switch (accessTokenRequest.getGrantType()) {
            case CLIENT_CREDENTIALS -> {
                return accessTokenService.fromClientCredentials(authorizationServer, accessTokenRequest);
            }
            case AUTHORIZATION_CODE -> {
                return accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest);
            }
        }
        throw new BadRequestException();    }

    @Override
    public Object authorizationServerIdUserinfoGet(UUID authorizationServerId, String idToken) {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
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
            final var user = userRepository.findById(UUID.fromString(jwtClaims.getSubject()));
            final var profile = profileRepository.findByResourceAndProfileType(user.get().getId(), ProfileType.User);

            return profile.getProfile();

        } catch (GeneralSecurityException | InvalidJwtException | MalformedClaimException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/authorization/")
    @Produces({ "text/html" })
    public Response authorizationPost(
            @QueryParam("response_type") @NotNull String responseType,
            @QueryParam("client_id") @NotNull String clientId,
            @QueryParam("redirect_uri") @NotNull String redirectUri,
            @QueryParam("scope") @NotNull String scope,
            @QueryParam("state") String state,
            @QueryParam("nonce") String nonce,
            @FormParam("email") @NotNull String email,
            @FormParam("password") @NotNull String password
    ) {
        final var code = clientService.buildClientCodeForEmailAndPassword(
                authorizationServerService.getDefaultAuthorizationServer(),
                clientId,
                email,
                password,
                nonce
        );
        if (code == null) {
            return Response.status(200).entity(Templates.login().render()).build();
        }
        return Response.status(302).location(
                URI.create(
                        redirectUri + "?code=" + code.getCode() + "&state=" + state + "&nonce=" + nonce + "?scope" + scope
                )
        ).build();
    }
}