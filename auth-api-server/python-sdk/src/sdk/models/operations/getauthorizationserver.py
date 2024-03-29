"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

from __future__ import annotations
import dataclasses
import requests as requests_http
from ..shared import authorizationserverresponse as shared_authorizationserverresponse
from typing import Optional


@dataclasses.dataclass
class GetAuthorizationServerRequest:
    
    authorization_server_id: str = dataclasses.field(metadata={'path_param': { 'field_name': 'authorizationServerId', 'style': 'simple', 'explode': False }})
    

@dataclasses.dataclass
class GetAuthorizationServerResponse:
    
    content_type: str = dataclasses.field()
    status_code: int = dataclasses.field()
    authorization_server_response: Optional[shared_authorizationserverresponse.AuthorizationServerResponse] = dataclasses.field(default=None)
    r"""Get an Authorization Server."""
    raw_response: Optional[requests_http.Response] = dataclasses.field(default=None)
    