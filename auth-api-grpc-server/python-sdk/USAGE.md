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