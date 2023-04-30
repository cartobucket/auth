# applications

## Overview

Applications are used for Server to Server communication. Each Application has a set of Scopes. An ApplicationSecret can be created for each Server and the ApplicationSecret can have the same set of Scopes as the Application, or a subset.

### Available Operations

* [create_application](#create_application)
* [create_application_secret](#create_application_secret)
* [delete_application](#delete_application)
* [delete_application_secret](#delete_application_secret)
* [get_application](#get_application)
* [list_application_secrets](#list_application_secrets)
* [list_applications](#list_applications)

## create_application

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.ApplicationRequest(
    authorization_server_id="magnam",
    client_id="debitis",
    client_secret="ipsa",
    name="Ricky Hoppe",
    profile={
        "voluptatum": "iusto",
        "excepturi": "nisi",
        "recusandae": "temporibus",
        "ab": "quis",
    },
)

res = s.applications.create_application(req)

if res.application_response is not None:
    # handle response
```

## create_application_secret

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.CreateApplicationSecretRequest(
    application_secret_request=shared.ApplicationSecretRequest(
        expires_in=87129,
        name="Christopher Hills",
        scopes="quo",
    ),
    application_id="odit",
)

res = s.applications.create_application_secret(req)

if res.application_secret_response is not None:
    # handle response
```

## delete_application

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteApplicationRequest(
    application_id="at",
)

res = s.applications.delete_application(req)

if res.status_code == 200:
    # handle response
```

## delete_application_secret

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteApplicationSecretRequest(
    application_id="at",
    secret_id="maiores",
)

res = s.applications.delete_application_secret(req)

if res.status_code == 200:
    # handle response
```

## get_application

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetApplicationRequest(
    application_id="molestiae",
)

res = s.applications.get_application(req)

if res.application_response is not None:
    # handle response
```

## list_application_secrets

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListApplicationSecretsRequest(
    application_id="quod",
)

res = s.applications.list_application_secrets(req)

if res.application_secrets_response is not None:
    # handle response
```

## list_applications

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListApplicationsRequest(
    authorization_server_ids=[
        "esse",
        "totam",
        "porro",
        "dolorum",
    ],
)

res = s.applications.list_applications(req)

if res.applications_response is not None:
    # handle response
```
