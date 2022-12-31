package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import com.cartobucket.auth.repositories.MockApplicationRepository;
import com.cartobucket.auth.repositories.MockClientCodeRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class AccessTokenServiceTests {

    @Inject
    AccessTokenService accessTokenService;

    @Inject
    AuthorizationServerService authorizationServerService;

    @Inject
    ApplicationService applicationService;

    @Test
    void testValidAuthorizationCodeRequest() {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();

        var accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClientId(String.valueOf(MockClientCodeRepository.VALID_CLIENT_ID));
        accessTokenRequest.setCode(MockClientCodeRepository.VALID_CLIENT_CODE);
        accessTokenRequest.setClientSecret("");
        accessTokenRequest.setCodeVerifier("TEST");
        accessTokenRequest.setRedirectUri("https://test");
        accessTokenRequest.setGrantType(AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE);
        var accessToken = accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest);
        assertNotNull(accessToken);
    }

    @Test
    void testInvalidAuthorizationCodeRequestWrongClientId() {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();

        var accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClientId(String.valueOf(UUID.randomUUID()));
        accessTokenRequest.setCode(MockClientCodeRepository.VALID_CLIENT_CODE);
        accessTokenRequest.setClientSecret("");
        accessTokenRequest.setCodeVerifier("TEST");
        accessTokenRequest.setRedirectUri("https://test");
        accessTokenRequest.setGrantType(AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE);
        var accessToken = accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest);
        assertNull(accessToken);
    }

    @Test
    void testInvalidAuthorizationCodeRequestWrongClientCode() {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();

        var accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClientId(String.valueOf(MockClientCodeRepository.VALID_CLIENT_ID));
        accessTokenRequest.setCode("FAIL");
        accessTokenRequest.setClientSecret("");
        accessTokenRequest.setCodeVerifier("TEST");
        accessTokenRequest.setRedirectUri("https://test");
        accessTokenRequest.setGrantType(AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE);
        var accessToken = accessTokenService.fromAuthorizationCode(authorizationServer, accessTokenRequest);
        assertNull(accessToken);
    }

    @Test
    void testValidClientCredentialRequest() {
        final var authorizationServer = authorizationServerService.getDefaultAuthorizationServer();
        final var applicationSecret = applicationService.createApplicationSecret(
                MockApplicationRepository.VALID_APPLICATION_ID,
                "test",
                Collections.EMPTY_LIST,
                300L
        );

        var accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClientId(MockApplicationRepository.VALID_CLIENT_ID);
        accessTokenRequest.setClientSecret(applicationSecret.getClientSecret());
        accessTokenRequest.setGrantType(AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS);
        var accessToken = accessTokenService.fromClientCredentials(authorizationServer, accessTokenRequest);
        assertNotNull(accessToken);
    }


}
