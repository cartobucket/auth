# authorizationServer

## Overview

This is where the interaction with the Authorization Server occurs.

### Available Operations

* [createAuthorizationCode](#createauthorizationcode)
* [getAuthorizationServerJwks](#getauthorizationserverjwks) - JWKS
* [getOpenIdConnectionWellKnown](#getopenidconnectionwellknown) - Well Known Endpoint
* [getUserInfo](#getuserinfo) - User Info
* [initiateAuthorization](#initiateauthorization) - Authorization Endpoint
* [issueToken](#issuetoken) - Token Endpoint

## createAuthorizationCode

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateAuthorizationCodeRequest;
import com.cartobucket.auth.api.models.operations.CreateAuthorizationCodeResponse;
import com.cartobucket.auth.api.models.operations.CreateAuthorizationCodeResponseTypeEnum;
import com.cartobucket.auth.api.models.shared.PasswordAuthRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            CreateAuthorizationCodeRequest req = new CreateAuthorizationCodeRequest(                new PasswordAuthRequest() {{
                                password = "sapiente";
                                username = "Orlando_Connelly97";
                            }};, "molestiae", "quod", CreateAuthorizationCodeResponseTypeEnum.CLIENT_CREDENTIALS) {{
                codeChallenge = "esse";
                codeChallengeMethod = "totam";
                nonce = "porro";
                redirectUri = "dolorum";
                scope = "dicta";
                state = "nam";
            }};            

            CreateAuthorizationCodeResponse res = sdk.authorizationServer.createAuthorizationCode(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getAuthorizationServerJwks

JWKS

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetAuthorizationServerJwksRequest;
import com.cartobucket.auth.api.models.operations.GetAuthorizationServerJwksResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetAuthorizationServerJwksRequest req = new GetAuthorizationServerJwksRequest("officia");            

            GetAuthorizationServerJwksResponse res = sdk.authorizationServer.getAuthorizationServerJwks(req);

            if (res.jwks != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getOpenIdConnectionWellKnown

Well Known Endpoint

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetOpenIdConnectionWellKnownRequest;
import com.cartobucket.auth.api.models.operations.GetOpenIdConnectionWellKnownResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetOpenIdConnectionWellKnownRequest req = new GetOpenIdConnectionWellKnownRequest("occaecati");            

            GetOpenIdConnectionWellKnownResponse res = sdk.authorizationServer.getOpenIdConnectionWellKnown(req);

            if (res.wellKnown != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getUserInfo

User Info

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetUserInfoRequest;
import com.cartobucket.auth.api.models.operations.GetUserInfoResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetUserInfoRequest req = new GetUserInfoRequest("fugit", "deleniti");            

            GetUserInfoResponse res = sdk.authorizationServer.getUserInfo(req);

            if (res.getUserInfo200ApplicationJSONObject != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## initiateAuthorization

Authorization Endpoint

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.InitiateAuthorizationRequest;
import com.cartobucket.auth.api.models.operations.InitiateAuthorizationResponse;
import com.cartobucket.auth.api.models.operations.InitiateAuthorizationResponseTypeEnum;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            InitiateAuthorizationRequest req = new InitiateAuthorizationRequest("hic", "optio", InitiateAuthorizationResponseTypeEnum.CLIENT_CREDENTIALS) {{
                codeChallenge = "beatae";
                codeChallengeMethod = "commodi";
                nonce = "molestiae";
                redirectUri = "modi";
                scope = "qui";
                state = "impedit";
            }};            

            InitiateAuthorizationResponse res = sdk.authorizationServer.initiateAuthorization(req);

            if (res.initiateAuthorization200TextHTMLString != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## issueToken

Token Endpoint

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.IssueTokenRequest;
import com.cartobucket.auth.api.models.operations.IssueTokenResponse;
import com.cartobucket.auth.api.models.shared.AccessTokenRequest;
import com.cartobucket.auth.api.models.shared.AccessTokenRequestGrantTypeEnum;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            IssueTokenRequest req = new IssueTokenRequest(                new AccessTokenRequest() {{
                                clientId = "cum";
                                clientSecret = "esse";
                                code = "ipsum";
                                codeVerifier = "excepturi";
                                grantType = AccessTokenRequestGrantTypeEnum.CLIENT_CREDENTIALS;
                                redirectUri = "perferendis";
                                scope = "ad";
                            }};, "natus");            

            IssueTokenResponse res = sdk.authorizationServer.issueToken(req);

            if (res.accessTokenResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
