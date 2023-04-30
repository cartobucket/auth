# openapi

<!-- Start SDK Installation -->
## SDK Installation

```bash
pip install git+<UNSET>.git
```
<!-- End SDK Installation -->

## SDK Example Usage
<!-- Start SDK Example Usage -->
```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.ApplicationRequest(
    authorization_server_id="corrupti",
    client_id="provident",
    client_secret="distinctio",
    name="Stuart Stiedemann",
    profile={
        "error": "deserunt",
        "suscipit": "iure",
    },
)

res = s.applications.create_application(req)

if res.application_response is not None:
    # handle response
```
<!-- End SDK Example Usage -->

<!-- Start SDK Available Operations -->
## Available Resources and Operations


### [applications](docs/applications/README.md)

* [create_application](docs/applications/README.md#create_application)
* [create_application_secret](docs/applications/README.md#create_application_secret)
* [delete_application](docs/applications/README.md#delete_application)
* [delete_application_secret](docs/applications/README.md#delete_application_secret)
* [get_application](docs/applications/README.md#get_application)
* [list_application_secrets](docs/applications/README.md#list_application_secrets)
* [list_applications](docs/applications/README.md#list_applications)

### [authorization_server](docs/authorizationserver/README.md)

* [create_authorization_code](docs/authorizationserver/README.md#create_authorization_code)
* [get_authorization_server_jwks](docs/authorizationserver/README.md#get_authorization_server_jwks) - JWKS
* [get_open_id_connection_well_known](docs/authorizationserver/README.md#get_open_id_connection_well_known) - Well Known Endpoint
* [get_user_info](docs/authorizationserver/README.md#get_user_info) - User Info
* [initiate_authorization](docs/authorizationserver/README.md#initiate_authorization) - Authorization Endpoint
* [issue_token](docs/authorizationserver/README.md#issue_token) - Token Endpoint

### [authorization_servers](docs/authorizationservers/README.md)

* [create_authorization_server](docs/authorizationservers/README.md#create_authorization_server)
* [delete_authorization_server](docs/authorizationservers/README.md#delete_authorization_server)
* [get_authorization_server](docs/authorizationservers/README.md#get_authorization_server)
* [list_authorization_servers](docs/authorizationservers/README.md#list_authorization_servers)
* [update_authorization_server](docs/authorizationservers/README.md#update_authorization_server)

### [clients](docs/clients/README.md)

* [create_client](docs/clients/README.md#create_client)
* [delete_client](docs/clients/README.md#delete_client)
* [get_client](docs/clients/README.md#get_client)
* [list_clients](docs/clients/README.md#list_clients)
* [update_client](docs/clients/README.md#update_client)

### [scopes](docs/scopes/README.md)

* [create_scope](docs/scopes/README.md#create_scope)
* [delete_scope](docs/scopes/README.md#delete_scope)
* [get_scope](docs/scopes/README.md#get_scope)
* [list_scopes](docs/scopes/README.md#list_scopes)

### [templates](docs/templates/README.md)

* [create_template](docs/templates/README.md#create_template)
* [delete_template](docs/templates/README.md#delete_template)
* [get_template](docs/templates/README.md#get_template)
* [list_templates](docs/templates/README.md#list_templates)
* [update_template](docs/templates/README.md#update_template)

### [users](docs/users/README.md)

* [create_user](docs/users/README.md#create_user)
* [delete_user](docs/users/README.md#delete_user)
* [get_user](docs/users/README.md#get_user)
* [list_users](docs/users/README.md#list_users)
* [update_user](docs/users/README.md#update_user)
<!-- End SDK Available Operations -->

### Maturity

This SDK is in beta, and there may be breaking changes between versions without a major version update. Therefore, we recommend pinning usage
to a specific package version. This way, you can install the same version each time without breaking changes unless you are intentionally
looking for the latest version.

### Contributions

While we value open-source contributions to this SDK, this library is generated programmatically.
Feel free to open a PR or a Github issue as a proof of concept and we'll do our best to include it in a future release !

### SDK Created by [Speakeasy](https://docs.speakeasyapi.dev/docs/using-speakeasy/client-sdks)
