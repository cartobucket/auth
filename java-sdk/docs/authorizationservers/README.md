# authorizationServers

## Overview

An AuthorizationServer is the top level domain model that grants AccessTokens.

### Available Operations

* [createAuthorizationServer](#createauthorizationserver)
* [deleteAuthorizationServer](#deleteauthorizationserver)
* [getAuthorizationServer](#getauthorizationserver)
* [listAuthorizationServers](#listauthorizationservers)
* [updateAuthorizationServer](#updateauthorizationserver)

## createAuthorizationServer

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateAuthorizationServerResponse;
import com.cartobucket.auth.api.models.shared.AuthorizationServerRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.AuthorizationServerRequest req = new AuthorizationServerRequest("sed", 612096, 222321, "natus", "laboriosam");            

            CreateAuthorizationServerResponse res = sdk.authorizationServers.createAuthorizationServer(req);

            if (res.authorizationServerResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteAuthorizationServer

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerRequest;
import com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteAuthorizationServerRequest req = new DeleteAuthorizationServerRequest("hic");            

            DeleteAuthorizationServerResponse res = sdk.authorizationServers.deleteAuthorizationServer(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getAuthorizationServer

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetAuthorizationServerRequest;
import com.cartobucket.auth.api.models.operations.GetAuthorizationServerResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetAuthorizationServerRequest req = new GetAuthorizationServerRequest("saepe");            

            GetAuthorizationServerResponse res = sdk.authorizationServers.getAuthorizationServer(req);

            if (res.authorizationServerResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listAuthorizationServers

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListAuthorizationServersResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListAuthorizationServersResponse res = sdk.authorizationServers.listAuthorizationServers();

            if (res.authorizationServersResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## updateAuthorizationServer

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerRequest;
import com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerResponse;
import com.cartobucket.auth.api.models.shared.AuthorizationServerRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            UpdateAuthorizationServerRequest req = new UpdateAuthorizationServerRequest(                new AuthorizationServerRequest("fuga", 449950, 359508, "iste", "iure");, "saepe");            

            UpdateAuthorizationServerResponse res = sdk.authorizationServers.updateAuthorizationServer(req);

            if (res.authorizationServerResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
