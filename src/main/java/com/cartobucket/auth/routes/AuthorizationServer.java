package com.cartobucket.auth.routes;

import com.cartobucket.auth.generated.AuthorizationServerApi;
import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.model.generated.AccessTokenResponse;
import com.cartobucket.auth.model.generated.JWKS;
import com.cartobucket.auth.model.generated.JWKSKeysInner;
import com.cartobucket.auth.models.ProfileType;
import com.cartobucket.auth.repositories.ProfileRepository;
import com.cartobucket.auth.repositories.UserRepository;
import com.cartobucket.auth.services.AccessTokenService;
import com.cartobucket.auth.services.AuthorizationServerService;
import com.cartobucket.auth.services.ClientService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.constraint.NotNull;

import javax.ws.rs.*;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.ParseException;
import java.util.*;

@Path("/authorizationServer/")
public class AuthorizationServer implements AuthorizationServerApi {
    final AccessTokenService accessTokenService;

    final AuthorizationServerService authorizationServerService;

    final ClientService clientService;

    final UserRepository userRepository;

    final ProfileRepository profileRepository;

    public AuthorizationServer(AccessTokenService accessTokenService, AuthorizationServerService authorizationServerService, ClientService clientService, UserRepository userRepository, ProfileRepository profileRepository) {
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
    public String authorizationGet(
            String responseType,
            String clientId,
            String redirectUri,
            String scope,
            String state
    ) {
        return Templates.login().render();
    }

    @POST
    @Path("/authorization/")
//    @Consumes({"text/html"})
    @Produces({ "text/html" })
    public Response authorizationPost(
            @QueryParam("response_type") @NotNull String responseType,
            @QueryParam("client_id") String clientId,
            @QueryParam("redirect_uri") String redirectUri,
            @QueryParam("scope") String scope,
            @QueryParam("state") String state,
            @QueryParam("nonce") String nonce,
            @FormParam("email") @NotNull String email,
            @FormParam("password") @NotNull String password
    ) {
        var code = clientService.getClientCodeForEmailAndPassword(
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
                        redirectUri + "?code=" + code.getCode() + "&state=" + state + "&nonce=" + nonce
                )
        ).build();
    }

    @Override
    public JWKS jwksGet() {
        var keys = new ArrayList<JWKSKeysInner>();
        for (var key : authorizationServerService.getJwksForAuthorizationServer(authorizationServerService.getDefaultAuthorizationServer()).getKeys()
        ) {
            var keysInner = new JWKSKeysInner();
            keysInner.setAlg(String.valueOf(key.getAlgorithm()));
            keysInner.setKid(key.getKeyID());
            keysInner.setN(String.valueOf(((RSAKey)key).getModulus()));
            keysInner.setE("AQAB");
            var obj  =key.toJSONObject();
            keysInner.setKty(String.valueOf(obj.get("kty")));
            keys.add(keysInner);
        }

        var jwks = new JWKS();
        jwks.setKeys(keys);
        return jwks;
    }

    @Override
    @Consumes({ "*/*" })
    public AccessTokenResponse tokenPost(AccessTokenRequest accessTokenRequest) {
        var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
        if (accessTokenRequest.getGrantType().equals(AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS)) {
            return accessTokenService.fromClientCredentials(authorizationServer, accessTokenRequest);
        }
        if (accessTokenRequest.getGrantType().equals(AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE)) {
            return accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest);
        }
        return new AccessTokenResponse();
    }

    @Override
    public Map<String, Object> userinfoGet(@HeaderParam("Authorization") String idToken) {
        try {
            var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
            final var jwks = authorizationServerService.getJwksForAuthorizationServer(authorizationServer);
            JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor =
                    new DefaultJWTProcessor<>();
            jwtProcessor.setJWSTypeVerifier(
                    new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("at+jwt")));
            JWSKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(expectedJWSAlg, new ImmutableJWKSet(jwks));
            jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier(
                    new JWTClaimsSet.Builder().issuer(authorizationServer.getServerUrl().toString()).build(),
                    new HashSet<>(Arrays.asList("sub", "iat", "exp", "scp", "cid", "jti"))));
            SecurityContext ctx = null; // optional context parameter, not required here

            var _idToken = idToken.replaceFirst("Bearer ", "");
            JWTClaimsSet claimsSet = jwtProcessor.process(_idToken, ctx);

            var user = userRepository.findById(UUID.fromString(claimsSet.getSubject()));
            var profile = profileRepository.findByResourceAndProfileType(user.get().getId(), ProfileType.User);

            var map = new HashMap<String, Object>();
            map.put("email", profile.getProfile().get("email"));
            return map;

        } catch (ParseException | JOSEException | BadJOSEException | NullPointerException e) {
            //throw new RuntimeException(e);
        }
        return null;
    }
}