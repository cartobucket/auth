"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

import requests as requests_http
from . import utils
from sdk.models import operations, shared
from typing import Optional

class Clients:
    r"""Clients are used to initiate and Authorization Code Flow. Currently only Username/Password logins are available."""
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
        
    def create_client(self, request: shared.ClientRequest) -> operations.CreateClientResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/clients/'
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        
        client = self._client
        
        http_res = client.request('POST', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.CreateClientResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ClientResponse])
                res.client_response = out

        return res

    def delete_client(self, request: operations.DeleteClientRequest) -> operations.DeleteClientResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.DeleteClientRequest, base_url, '/clients/{clientId}', request)
        
        
        client = self._client
        
        http_res = client.request('DELETE', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.DeleteClientResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        

        return res

    def get_client(self, request: operations.GetClientRequest) -> operations.GetClientResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetClientRequest, base_url, '/clients/{clientId}', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetClientResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ClientResponse])
                res.client_response = out

        return res

    def list_clients(self, request: operations.ListClientsRequest) -> operations.ListClientsResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/clients/'
        
        query_params = utils.get_query_params(operations.ListClientsRequest, request)
        
        client = self._client
        
        http_res = client.request('GET', url, params=query_params)
        content_type = http_res.headers.get('Content-Type')

        res = operations.ListClientsResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ClientsResponse])
                res.clients_response = out

        return res

    def update_client(self, request: operations.UpdateClientRequest) -> operations.UpdateClientResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.UpdateClientRequest, base_url, '/clients/{clientId}', request)
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "client_request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        if data is None and form is None:
            raise Exception('request body is required')
        
        client = self._client
        
        http_res = client.request('PUT', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.UpdateClientResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ClientResponse])
                res.client_response = out

        return res

    