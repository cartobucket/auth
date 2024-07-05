# applications

## Overview

Applications are used for Server to Server communication. Each Application has a set of Scopes. An ApplicationSecret can be created for each Server and the ApplicationSecret can have the same set of Scopes as the Application, or a subset.

### Available Operations

* [createApplication](#createapplication)
* [createApplicationSecret](#createapplicationsecret)
* [deleteApplication](#deleteapplication)
* [deleteApplicationSecret](#deleteapplicationsecret)
* [getApplication](#getapplication)
* [listApplicationSecrets](#listapplicationsecrets)
* [listApplications](#listapplications)

## createApplication

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateApplicationResponse;
import com.cartobucket.auth.api.models.shared.ApplicationRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.ApplicationRequest req = new ApplicationRequest("suscipit", "iure") {{
                clientId = "magnam";
                clientSecret = "debitis";
                profile = new java.util.HashMap<String, Object>() {{
                    put("delectus", "tempora");
                }};
            }};            

            CreateApplicationResponse res = sdk.applications.createApplication(req);

            if (res.applicationResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## createApplicationSecret

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateApplicationSecretRequest;
import com.cartobucket.auth.api.models.operations.CreateApplicationSecretResponse;
import com.cartobucket.auth.api.models.shared.ApplicationSecretRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            CreateApplicationSecretRequest req = new CreateApplicationSecretRequest("suscipit") {{
                applicationSecretRequest = new ApplicationSecretRequest() {{
                    expiresIn = 477665;
                    name = "Irving Lehner";
                    scopes = "nisi";
                }};;
            }};            

            CreateApplicationSecretResponse res = sdk.applications.createApplicationSecret(req);

            if (res.applicationSecretResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteApplication

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteApplicationRequest;
import com.cartobucket.auth.api.models.operations.DeleteApplicationResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteApplicationRequest req = new DeleteApplicationRequest("recusandae");            

            DeleteApplicationResponse res = sdk.applications.deleteApplication(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteApplicationSecret

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteApplicationSecretRequest;
import com.cartobucket.auth.api.models.operations.DeleteApplicationSecretResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteApplicationSecretRequest req = new DeleteApplicationSecretRequest("temporibus", "ab");            

            DeleteApplicationSecretResponse res = sdk.applications.deleteApplicationSecret(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getApplication

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetApplicationRequest;
import com.cartobucket.auth.api.models.operations.GetApplicationResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetApplicationRequest req = new GetApplicationRequest("quis");            

            GetApplicationResponse res = sdk.applications.getApplication(req);

            if (res.applicationResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listApplicationSecrets

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListApplicationSecretsRequest;
import com.cartobucket.auth.api.models.operations.ListApplicationSecretsResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListApplicationSecretsRequest req = new ListApplicationSecretsRequest("veritatis");            

            ListApplicationSecretsResponse res = sdk.applications.listApplicationSecrets(req);

            if (res.applicationSecretsResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listApplications

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListApplicationsRequest;
import com.cartobucket.auth.api.models.operations.ListApplicationsResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListApplicationsRequest req = new ListApplicationsRequest() {{
                authorizationServerIds = new String[]{{
                    add("perferendis"),
                    add("ipsam"),
                    add("repellendus"),
                }};
            }};            

            ListApplicationsResponse res = sdk.applications.listApplications(req);

            if (res.applicationsResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
