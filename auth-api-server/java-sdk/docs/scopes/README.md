# scopes

## Overview

Scopes are a set of tags used to indicate Authorization. Currently only static Scopes are supported.

### Available Operations

* [createScope](#createscope)
* [deleteScope](#deletescope)
* [getScope](#getscope)
* [listScopes](#listscopes)

## createScope

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateScopeResponse;
import com.cartobucket.auth.api.models.shared.ScopeRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.ScopeRequest req = new ScopeRequest("mollitia", "occaecati");            

            CreateScopeResponse res = sdk.scopes.createScope(req);

            if (res.scopeResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteScope

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteScopeRequest;
import com.cartobucket.auth.api.models.operations.DeleteScopeResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteScopeRequest req = new DeleteScopeRequest("numquam");            

            DeleteScopeResponse res = sdk.scopes.deleteScope(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getScope

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetScopeRequest;
import com.cartobucket.auth.api.models.operations.GetScopeResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetScopeRequest req = new GetScopeRequest("commodi");            

            GetScopeResponse res = sdk.scopes.getScope(req);

            if (res.scopeResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listScopes

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListScopesRequest;
import com.cartobucket.auth.api.models.operations.ListScopesResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListScopesRequest req = new ListScopesRequest() {{
                authorizationServerIds = new String[]{{
                    add("molestiae"),
                    add("velit"),
                }};
            }};            

            ListScopesResponse res = sdk.scopes.listScopes(req);

            if (res.scopesResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
