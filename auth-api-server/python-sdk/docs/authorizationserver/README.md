# authorization_server

## Overview

This is where the interaction with the Authorization Server occurs.

### Available Operations

* [create_authorization_code](#create_authorization_code)
* [get_authorization_server_jwks](#get_authorization_server_jwks) - JWKS
* [get_open_id_connection_well_known](#get_open_id_connection_well_known) - Well Known Endpoint
* [get_user_info](#get_user_info) - User Info
* [initiate_authorization](#initiate_authorization) - Authorization Endpoint
* [issue_token](#issue_token) - Token Endpoint

## create_authorization_code

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.CreateAuthorizationCodeRequest(
    password_auth_request=shared.PasswordAuthRequest(
        password="dicta",
        username="Minerva.Nikolaus",
    ),
    authorization_server_id="deleniti",
    client_id="hic",
    code_challenge="optio",
    code_challenge_method="totam",
    nonce="beatae",
    redirect_uri="commodi",
    response_type="code",
    scope="modi",
    state="qui",
)

res = s.authorization_server.create_authorization_code(req)

if res.status_code == 200:
    # handle response
```

## get_authorization_server_jwks

JWKS

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetAuthorizationServerJwksRequest(
    authorization_server_id="impedit",
)

res = s.authorization_server.get_authorization_server_jwks(req)

if res.jwks is not None:
    # handle response
```

## get_open_id_connection_well_known

Well Known Endpoint

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetOpenIDConnectionWellKnownRequest(
    authorization_server_id="cum",
)

res = s.authorization_server.get_open_id_connection_well_known(req)

if res.well_known is not None:
    # handle response
```

## get_user_info

User Info

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetUserInfoRequest(
    authorization="esse",
    authorization_server_id="ipsum",
)

res = s.authorization_server.get_user_info(req)

if res.get_user_info_200_application_json_object is not None:
    # handle response
```

## initiate_authorization

Authorization Endpoint

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.InitiateAuthorizationRequest(
    authorization_server_id="excepturi",
    client_id="aspernatur",
    code_challenge="perferendis",
    code_challenge_method="ad",
    nonce="natus",
    redirect_uri="sed",
    response_type="client_credentials",
    scope="dolor",
    state="natus",
)

res = s.authorization_server.initiate_authorization(req)

if res.initiate_authorization_200_text_html_string is not None:
    # handle response
```

## issue_token

Token Endpoint

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.IssueTokenRequest(
    access_token_request=shared.AccessTokenRequest(
        client_id="laboriosam",
        client_secret="hic",
        code="saepe",
        code_verifier="fuga",
        grant_type="client_credentials",
        redirect_uri="corporis",
        scope="iste",
    ),
    authorization_server_id="iure",
)

res = s.authorization_server.issue_token(req)

if res.access_token_response is not None:
    # handle response
```
