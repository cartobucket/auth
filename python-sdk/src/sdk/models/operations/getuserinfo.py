"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

from __future__ import annotations
import dataclasses
import requests as requests_http
from typing import Any, Optional


@dataclasses.dataclass
class GetUserInfoRequest:
    
    authorization: str = dataclasses.field(metadata={'header': { 'field_name': 'Authorization', 'style': 'simple', 'explode': False }})
    authorization_server_id: str = dataclasses.field(metadata={'path_param': { 'field_name': 'authorizationServerId', 'style': 'simple', 'explode': False }})
    

@dataclasses.dataclass
class GetUserInfoResponse:
    
    content_type: str = dataclasses.field()
    status_code: int = dataclasses.field()
    get_user_info_200_application_json_object: Optional[dict[str, Any]] = dataclasses.field(default=None)
    r"""Get userinfo"""
    raw_response: Optional[requests_http.Response] = dataclasses.field(default=None)
    