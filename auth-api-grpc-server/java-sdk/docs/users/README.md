# users

## Overview

Users represent a person entity in the system. The User domain model is used in the Authorization Code Flow and can be granted Access Tokens through a Client.

### Available Operations

* [createUser](#createuser)
* [deleteUser](#deleteuser)
* [getUser](#getuser)
* [listUsers](#listusers)
* [updateUser](#updateuser)

## createUser

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateUserResponse;
import com.cartobucket.auth.api.models.shared.UserRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.UserRequest req = new UserRequest() {{
                authorizationServerId = "ipsam";
                email = "Reid62@yahoo.com";
                profile = new java.util.HashMap<String, Object>() {{
                    put("laborum", "quasi");
                    put("reiciendis", "voluptatibus");
                    put("vero", "nihil");
                    put("praesentium", "voluptatibus");
                }};
                username = "Ana_Moen";
            }};            

            CreateUserResponse res = sdk.users.createUser(req);

            if (res.userResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteUser

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteUserRequest;
import com.cartobucket.auth.api.models.operations.DeleteUserResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteUserRequest req = new DeleteUserRequest("perferendis");            

            DeleteUserResponse res = sdk.users.deleteUser(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getUser

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetUserRequest;
import com.cartobucket.auth.api.models.operations.GetUserResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetUserRequest req = new GetUserRequest("doloremque");            

            GetUserResponse res = sdk.users.getUser(req);

            if (res.userResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listUsers

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListUsersRequest;
import com.cartobucket.auth.api.models.operations.ListUsersResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListUsersRequest req = new ListUsersRequest() {{
                authorizationServerIds = new String[]{{
                    add("ut"),
                    add("maiores"),
                }};
            }};            

            ListUsersResponse res = sdk.users.listUsers(req);

            if (res.usersResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## updateUser

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.UpdateUserRequest;
import com.cartobucket.auth.api.models.operations.UpdateUserResponse;
import com.cartobucket.auth.api.models.shared.UserRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            UpdateUserRequest req = new UpdateUserRequest(                new UserRequest() {{
                                authorizationServerId = "dicta";
                                email = "Elena68@yahoo.com";
                                profile = new java.util.HashMap<String, Object>() {{
                                    put("accusamus", "commodi");
                                    put("repudiandae", "quae");
                                }};
                                username = "Curt_Pouros";
                            }};, "pariatur");            

            UpdateUserResponse res = sdk.users.updateUser(req);

            if (res.userResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
