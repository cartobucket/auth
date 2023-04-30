# authorization_servers

## Overview

An AuthorizationServer is the top level domain model that grants AccessTokens.

### Available Operations

* [create_authorization_server](#create_authorization_server)
* [delete_authorization_server](#delete_authorization_server)
* [get_authorization_server](#get_authorization_server)
* [list_authorization_servers](#list_authorization_servers)
* [update_authorization_server](#update_authorization_server)

## create_authorization_server

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.AuthorizationServerRequest(
    audience="saepe",
    authorization_code_token_expiration=697631,
    client_credentials_token_expiration=99280,
    name="Lela Orn",
    server_url="dolores",
)

res = s.authorization_servers.create_authorization_server(req)

if res.authorization_server_response is not None:
    # handle response
```

## delete_authorization_server

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteAuthorizationServerRequest(
    authorization_server_id="dolorem",
)

res = s.authorization_servers.delete_authorization_server(req)

if res.status_code == 200:
    # handle response
```

## get_authorization_server

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetAuthorizationServerRequest(
    authorization_server_id="corporis",
)

res = s.authorization_servers.get_authorization_server(req)

if res.authorization_server_response is not None:
    # handle response
```

## list_authorization_servers

### Example Usage

```python
import sdk


s = sdk.SDK()


res = s.authorization_servers.list_authorization_servers()

if res.authorization_servers_response is not None:
    # handle response
```

## update_authorization_server

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.UpdateAuthorizationServerRequest(
    authorization_server_request=shared.AuthorizationServerRequest(
        audience="explicabo",
        authorization_code_token_expiration=750686,
        client_credentials_token_expiration=315428,
        name="Corey Hane III",
        server_url="culpa",
    ),
    authorization_server_id="doloribus",
)

res = s.authorization_servers.update_authorization_server(req)

if res.authorization_server_response is not None:
    # handle response
```
