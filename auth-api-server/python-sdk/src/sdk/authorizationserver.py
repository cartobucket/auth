"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

import requests as requests_http
from . import utils
from sdk.models import operations, shared
from typing import Any, Optional

class AuthorizationServer:
    r"""This is where the interaction with the Authorization Server occurs."""
    _client: requests_http.Session
    _security_client: requests_http.Session
    _server_url: str
    _language: str
    _sdk_version: str
    _gen_version: str

    def __init__(self, client: requests_http.Session, security_client: requests_http.Session, server_url: str, language: str, sdk_version: str, gen_version: str) -> None:
        self._client = client
        self._security_client = security_client
        self._server_url = server_url
        self._language = language
        self._sdk_version = sdk_version
        self._gen_version = gen_version
        
    def create_authorization_code(self, request: operations.CreateAuthorizationCodeRequest) -> operations.CreateAuthorizationCodeResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.CreateAuthorizationCodeRequest, base_url, '/{authorizationServerId}/authorization/', request)
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "password_auth_request", 'multipart')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        query_params = utils.get_query_params(operations.CreateAuthorizationCodeRequest, request)
        
        client = self._client
        
        http_res = client.request('POST', url, params=query_params, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.CreateAuthorizationCodeResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        

        return res

    def get_authorization_server_jwks(self, request: operations.GetAuthorizationServerJwksRequest) -> operations.GetAuthorizationServerJwksResponse:
        r"""JWKS"""
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetAuthorizationServerJwksRequest, base_url, '/{authorizationServerId}/jwks/', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetAuthorizationServerJwksResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.Jwks])
                res.jwks = out

        return res

    def get_open_id_connection_well_known(self, request: operations.GetOpenIDConnectionWellKnownRequest) -> operations.GetOpenIDConnectionWellKnownResponse:
        r"""Well Known Endpoint"""
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetOpenIDConnectionWellKnownRequest, base_url, '/{authorizationServerId}/.well-known/openid-connect/', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetOpenIDConnectionWellKnownResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.WellKnown])
                res.well_known = out

        return res

    def get_user_info(self, request: operations.GetUserInfoRequest) -> operations.GetUserInfoResponse:
        r"""User Info"""
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetUserInfoRequest, base_url, '/{authorizationServerId}/userinfo/', request)
        
        headers = utils.get_headers(request)
        
        client = self._client
        
        http_res = client.request('GET', url, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetUserInfoResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[dict[str, Any]])
                res.get_user_info_200_application_json_object = out

        return res

    def initiate_authorization(self, request: operations.InitiateAuthorizationRequest) -> operations.InitiateAuthorizationResponse:
        r"""Authorization Endpoint"""
        base_url = self._server_url
        
        url = utils.generate_url(operations.InitiateAuthorizationRequest, base_url, '/{authorizationServerId}/authorization/', request)
        
        query_params = utils.get_query_params(operations.InitiateAuthorizationRequest, request)
        
        client = self._client
        
        http_res = client.request('GET', url, params=query_params)
        content_type = http_res.headers.get('Content-Type')

        res = operations.InitiateAuthorizationResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'text/html'):
                res.initiate_authorization_200_text_html_string = http_res.content

        return res

    def issue_token(self, request: operations.IssueTokenRequest) -> operations.IssueTokenResponse:
        r"""Token Endpoint"""
        base_url = self._server_url
        
        url = utils.generate_url(operations.IssueTokenRequest, base_url, '/{authorizationServerId}/token/', request)
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "access_token_request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        
        client = self._client
        
        http_res = client.request('POST', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.IssueTokenResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.AccessTokenResponse])
                res.access_token_response = out

        return res

    