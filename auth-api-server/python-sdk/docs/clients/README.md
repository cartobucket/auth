# clients

## Overview

Clients are used to initiate and Authorization Code Flow. Currently only Username/Password logins are available.

### Available Operations

* [create_client](#create_client)
* [delete_client](#delete_client)
* [get_client](#get_client)
* [list_clients](#list_clients)
* [update_client](#update_client)

## create_client

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.ClientRequest(
    authorization_server_id="sapiente",
    name="Angie Durgan",
    redirect_uris=[
        "mollitia",
        "occaecati",
        "numquam",
        "commodi",
    ],
    scopes="quam",
)

res = s.clients.create_client(req)

if res.client_response is not None:
    # handle response
```

## delete_client

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteClientRequest(
    client_id="molestiae",
)

res = s.clients.delete_client(req)

if res.status_code == 200:
    # handle response
```

## get_client

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetClientRequest(
    client_id="velit",
)

res = s.clients.get_client(req)

if res.client_response is not None:
    # handle response
```

## list_clients

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListClientsRequest(
    authorization_server_ids=[
        "quia",
        "quis",
        "vitae",
    ],
)

res = s.clients.list_clients(req)

if res.clients_response is not None:
    # handle response
```

## update_client

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.UpdateClientRequest(
    client_request=shared.ClientRequest(
        authorization_server_id="laborum",
        name="Bill Conn",
        redirect_uris=[
            "ipsam",
            "id",
            "possimus",
            "aut",
        ],
        scopes="quasi",
    ),
    client_id="error",
)

res = s.clients.update_client(req)

if res.client_response is not None:
    # handle response
```
