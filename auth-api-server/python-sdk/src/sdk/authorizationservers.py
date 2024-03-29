"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

import requests as requests_http
from . import utils
from sdk.models import operations, shared
from typing import Optional

class AuthorizationServers:
    r"""An AuthorizationServer is the top level domain model that grants AccessTokens."""
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
        
    def create_authorization_server(self, request: shared.AuthorizationServerRequest) -> operations.CreateAuthorizationServerResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/authorizationServers/'
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        
        client = self._client
        
        http_res = client.request('POST', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.CreateAuthorizationServerResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.AuthorizationServerResponse])
                res.authorization_server_response = out

        return res

    def delete_authorization_server(self, request: operations.DeleteAuthorizationServerRequest) -> operations.DeleteAuthorizationServerResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.DeleteAuthorizationServerRequest, base_url, '/authorizationServers/{authorizationServerId}', request)
        
        
        client = self._client
        
        http_res = client.request('DELETE', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.DeleteAuthorizationServerResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        

        return res

    def get_authorization_server(self, request: operations.GetAuthorizationServerRequest) -> operations.GetAuthorizationServerResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetAuthorizationServerRequest, base_url, '/authorizationServers/{authorizationServerId}', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetAuthorizationServerResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.AuthorizationServerResponse])
                res.authorization_server_response = out

        return res

    def list_authorization_servers(self) -> operations.ListAuthorizationServersResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/authorizationServers/'
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.ListAuthorizationServersResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.AuthorizationServersResponse])
                res.authorization_servers_response = out

        return res

    def update_authorization_server(self, request: operations.UpdateAuthorizationServerRequest) -> operations.UpdateAuthorizationServerResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.UpdateAuthorizationServerRequest, base_url, '/authorizationServers/{authorizationServerId}', request)
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "authorization_server_request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        
        client = self._client
        
        http_res = client.request('PUT', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.UpdateAuthorizationServerResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.AuthorizationServerResponse])
                res.authorization_server_response = out

        return res

    