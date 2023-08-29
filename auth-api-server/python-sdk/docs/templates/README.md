# templates

## Overview

Templates are used to render information such as the login page.

### Available Operations

* [create_template](#create_template)
* [delete_template](#delete_template)
* [get_template](#get_template)
* [list_templates](#list_templates)
* [update_template](#update_template)

## create_template

### Example Usage

```python
import sdk
from sdk.models import shared

s = sdk.SDK()


req = shared.TemplateRequest(
    authorization_server_id="perferendis",
    template="doloremque",
    template_type="login",
)

res = s.templates.create_template(req)

if res.template_response is not None:
    # handle response
```

## delete_template

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.DeleteTemplateRequest(
    template_id="reprehenderit",
)

res = s.templates.delete_template(req)

if res.status_code == 200:
    # handle response
```

## get_template

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.GetTemplateRequest(
    template_id="ut",
)

res = s.templates.get_template(req)

if res.template_response is not None:
    # handle response
```

## list_templates

### Example Usage

```python
import sdk
from sdk.models import operations

s = sdk.SDK()


req = operations.ListTemplatesRequest(
    authorization_server_ids=[
        "dicta",
        "corporis",
        "dolore",
        "iusto",
    ],
)

res = s.templates.list_templates(req)

if res.template_response is not None:
    # handle response
```

## update_template

### Example Usage

```python
import sdk
from sdk.models import operations, shared

s = sdk.SDK()


req = operations.UpdateTemplateRequest(
    template_request=shared.TemplateRequest(
        authorization_server_id="dicta",
        template="harum",
        template_type="login",
    ),
    template_id="enim",
)

res = s.templates.update_template(req)

if res.template_response is not None:
    # handle response
```
