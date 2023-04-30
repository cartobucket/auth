# scopes

## Overview

Scopes are a set of tags used to indicate Authorization. Currently only static Scopes are supported.

### Available Operations

* [create_scope](#create_scope)
* [delete_scope](#delete_scope)
* [get_scope](#get_scope)
* [list_scopes](#list_scopes)

## create_scope

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.ScopeRequest(
    authorization_server_id="temporibus",
    name="Ryan Witting",
)

res = s.scopes.create_scope(req)

if res.scope_response is not None:
    # handle response
```

## delete_scope

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteScopeRequest(
    scope_id="nihil",
)

res = s.scopes.delete_scope(req)

if res.status_code == 200:
    # handle response
```

## get_scope

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetScopeRequest(
    scope_id="praesentium",
)

res = s.scopes.get_scope(req)

if res.scope_response is not None:
    # handle response
```

## list_scopes

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListScopesRequest(
    authorization_server_ids=[
        "ipsa",
        "omnis",
        "voluptate",
        "cum",
    ],
)

res = s.scopes.list_scopes(req)

if res.scopes_response is not None:
    # handle response
```
