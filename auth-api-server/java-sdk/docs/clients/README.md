# clients

## Overview

Clients are used to initiate and Authorization Code Flow. Currently only Username/Password logins are available.

### Available Operations

* [createClient](#createclient)
* [deleteClient](#deleteclient)
* [getClient](#getclient)
* [listClients](#listclients)
* [updateClient](#updateclient)

## createClient

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateClientResponse;
import com.cartobucket.auth.api.models.shared.ClientRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.ClientRequest req = new ClientRequest() {{
                authorizationServerId = "quidem";
                name = "Brenda Wisozk";
                redirectUris = new String[]{{
                    add("dolores"),
                    add("dolorem"),
                    add("corporis"),
                }};
                scopes = "explicabo";
            }};            

            CreateClientResponse res = sdk.clients.createClient(req);

            if (res.clientResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteClient

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteClientRequest;
import com.cartobucket.auth.api.models.operations.DeleteClientResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteClientRequest req = new DeleteClientRequest("nobis");            

            DeleteClientResponse res = sdk.clients.deleteClient(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getClient

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetClientRequest;
import com.cartobucket.auth.api.models.operations.GetClientResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetClientRequest req = new GetClientRequest("enim");            

            GetClientResponse res = sdk.clients.getClient(req);

            if (res.clientResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listClients

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListClientsRequest;
import com.cartobucket.auth.api.models.operations.ListClientsResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListClientsRequest req = new ListClientsRequest() {{
                authorizationServerIds = new String[]{{
                    add("nemo"),
                    add("minima"),
                    add("excepturi"),
                }};
            }};            

            ListClientsResponse res = sdk.clients.listClients(req);

            if (res.clientsResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## updateClient

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.UpdateClientRequest;
import com.cartobucket.auth.api.models.operations.UpdateClientResponse;
import com.cartobucket.auth.api.models.shared.ClientRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            UpdateClientRequest req = new UpdateClientRequest(                new ClientRequest() {{
                                authorizationServerId = "accusantium";
                                name = "Cecilia Yundt MD";
                                redirectUris = new String[]{{
                                    add("culpa"),
                                }};
                                scopes = "consequuntur";
                            }};, "repellat");            

            UpdateClientResponse res = sdk.clients.updateClient(req);

            if (res.clientResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
