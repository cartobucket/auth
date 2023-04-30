# users

## Overview

Users represent a person entity in the system. The User domain model is used in the Authorization Code Flow and can be granted Access Tokens through a Client.

### Available Operations

* [create_user](#create_user)
* [delete_user](#delete_user)
* [get_user](#get_user)
* [list_users](#list_users)
* [update_user](#update_user)

## create_user

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.UserRequest(
    authorization_server_id="accusamus",
    email="Terrill69@yahoo.com",
    profile={
        "excepturi": "pariatur",
        "modi": "praesentium",
        "rem": "voluptates",
    },
    username="Aurelia.Waelchi",
)

res = s.users.create_user(req)

if res.user_response is not None:
    # handle response
```

## delete_user

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteUserRequest(
    user_id="itaque",
)

res = s.users.delete_user(req)

if res.status_code == 200:
    # handle response
```

## get_user

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetUserRequest(
    user_id="incidunt",
)

res = s.users.get_user(req)

if res.user_response is not None:
    # handle response
```

## list_users

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListUsersRequest(
    authorization_server_ids=[
        "consequatur",
        "est",
    ],
)

res = s.users.list_users(req)

if res.users_response is not None:
    # handle response
```

## update_user

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.UpdateUserRequest(
    user_request=shared.UserRequest(
        authorization_server_id="quibusdam",
        email="Luther.Rau26@gmail.com",
        profile={
            "aliquid": "cupiditate",
        },
        username="Kavon82",
    ),
    user_id="ipsam",
)

res = s.users.update_user(req)

if res.user_response is not None:
    # handle response
```
