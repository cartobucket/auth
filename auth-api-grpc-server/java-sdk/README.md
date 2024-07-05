# openapi

<!-- Start SDK Installation -->
## SDK Installation

### Gradle

```groovy
implementation 'com.cartobucket.auth.api:Cartobucket Auth:0.0.1'
```
<!-- End SDK Installation -->

## SDK Example Usage
<!-- Start SDK Example Usage -->
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

            com.cartobucket.auth.api.models.shared.ApplicationRequest req = new ApplicationRequest("corrupti", "provident") {{
                clientId = "distinctio";
                clientSecret = "quibusdam";
                profile = new java.util.HashMap<String, Object>() {{
                    put("nulla", "corrupti");
                    put("illum", "vel");
                    put("error", "deserunt");
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
<!-- End SDK Example Usage -->

<!-- Start SDK Available Operations -->
## Available Resources and Operations


### [applications](docs/applications/README.md)

* [createApplication](docs/applications/README.md#createapplication)
* [createApplicationSecret](docs/applications/README.md#createapplicationsecret)
* [deleteApplication](docs/applications/README.md#deleteapplication)
* [deleteApplicationSecret](docs/applications/README.md#deleteapplicationsecret)
* [getApplication](docs/applications/README.md#getapplication)
* [listApplicationSecrets](docs/applications/README.md#listapplicationsecrets)
* [listApplications](docs/applications/README.md#listapplications)

### [authorizationServer](docs/authorizationserver/README.md)

* [createAuthorizationCode](docs/authorizationserver/README.md#createauthorizationcode)
* [getAuthorizationServerJwks](docs/authorizationserver/README.md#getauthorizationserverjwks) - JWKS
* [getOpenIdConnectionWellKnown](docs/authorizationserver/README.md#getopenidconnectionwellknown) - Well Known Endpoint
* [getUserInfo](docs/authorizationserver/README.md#getuserinfo) - User Info
* [initiateAuthorization](docs/authorizationserver/README.md#initiateauthorization) - Authorization Endpoint
* [issueToken](docs/authorizationserver/README.md#issuetoken) - Token Endpoint

### [authorizationServers](docs/authorizationservers/README.md)

* [createAuthorizationServer](docs/authorizationservers/README.md#createauthorizationserver)
* [deleteAuthorizationServer](docs/authorizationservers/README.md#deleteauthorizationserver)
* [getAuthorizationServer](docs/authorizationservers/README.md#getauthorizationserver)
* [listAuthorizationServers](docs/authorizationservers/README.md#listauthorizationservers)
* [updateAuthorizationServer](docs/authorizationservers/README.md#updateauthorizationserver)

### [clients](docs/clients/README.md)

* [createClient](docs/clients/README.md#createclient)
* [deleteClient](docs/clients/README.md#deleteclient)
* [getClient](docs/clients/README.md#getclient)
* [listClients](docs/clients/README.md#listclients)
* [updateClient](docs/clients/README.md#updateclient)

### [scopes](docs/scopes/README.md)

* [createScope](docs/scopes/README.md#createscope)
* [deleteScope](docs/scopes/README.md#deletescope)
* [getScope](docs/scopes/README.md#getscope)
* [listScopes](docs/scopes/README.md#listscopes)

### [templates](docs/templates/README.md)

* [createTemplate](docs/templates/README.md#createtemplate)
* [deleteTemplate](docs/templates/README.md#deletetemplate)
* [getTemplate](docs/templates/README.md#gettemplate)
* [listTemplates](docs/templates/README.md#listtemplates)
* [updateTemplate](docs/templates/README.md#updatetemplate)

### [users](docs/users/README.md)

* [createUser](docs/users/README.md#createuser)
* [deleteUser](docs/users/README.md#deleteuser)
* [getUser](docs/users/README.md#getuser)
* [listUsers](docs/users/README.md#listusers)
* [updateUser](docs/users/README.md#updateuser)
<!-- End SDK Available Operations -->

### Maturity

This SDK is in beta, and there may be breaking changes between versions without a major version update. Therefore, we recommend pinning usage 
to a specific package version. This way, you can install the same version each time without breaking changes unless you are intentionally 
looking for the latest version.

### Contributions

While we value open-source contributions to this SDK, this library is generated programmatically. 
Feel free to open a PR or a Github issue as a proof of concept and we'll do our best to include it in a future release !

### SDK Created by [Speakeasy](https://docs.speakeasyapi.dev/docs/using-speakeasy/client-sdks)
