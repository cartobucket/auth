"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

from __future__ import annotations
import dataclasses
import requests as requests_http
from ..shared import applicationsresponse as shared_applicationsresponse
from typing import Optional


@dataclasses.dataclass
class ListApplicationsRequest:
    
    authorization_server_ids: Optional[list[str]] = dataclasses.field(default=None, metadata={'query_param': { 'field_name': 'authorizationServerIds', 'style': 'form', 'explode': True }})
    

@dataclasses.dataclass
class ListApplicationsResponse:
    
    content_type: str = dataclasses.field()
    status_code: int = dataclasses.field()
    applications_response: Optional[shared_applicationsresponse.ApplicationsResponse] = dataclasses.field(default=None)
    r"""Get Applications"""
    raw_response: Optional[requests_http.Response] = dataclasses.field(default=None)
    