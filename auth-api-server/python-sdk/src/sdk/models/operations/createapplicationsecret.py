"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

from __future__ import annotations
import dataclasses
import requests as requests_http
from ..shared import applicationsecretrequest as shared_applicationsecretrequest
from ..shared import applicationsecretresponse as shared_applicationsecretresponse
from typing import Optional


@dataclasses.dataclass
class CreateApplicationSecretRequest:
    
    application_id: str = dataclasses.field(metadata={'path_param': { 'field_name': 'applicationId', 'style': 'simple', 'explode': False }})
    application_secret_request: Optional[shared_applicationsecretrequest.ApplicationSecretRequest] = dataclasses.field(default=None, metadata={'request': { 'media_type': 'application/json' }})
    

@dataclasses.dataclass
class CreateApplicationSecretResponse:
    
    content_type: str = dataclasses.field()
    status_code: int = dataclasses.field()
    application_secret_response: Optional[shared_applicationsecretresponse.ApplicationSecretResponse] = dataclasses.field(default=None)
    r"""Create an application secret"""
    raw_response: Optional[requests_http.Response] = dataclasses.field(default=None)
    