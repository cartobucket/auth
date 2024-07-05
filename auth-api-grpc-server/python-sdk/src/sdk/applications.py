"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

import requests as requests_http
from . import utils
from sdk.models import operations, shared
from typing import Optional

class Applications:
    r"""Applications are used for Server to Server communication. Each Application has a set of Scopes. An ApplicationSecret can be created for each Server and the ApplicationSecret can have the same set of Scopes as the Application, or a subset."""
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
        
    def create_application(self, request: shared.ApplicationRequest) -> operations.CreateApplicationResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/applications/'
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        
        client = self._client
        
        http_res = client.request('POST', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.CreateApplicationResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ApplicationResponse])
                res.application_response = out

        return res

    def create_application_secret(self, request: operations.CreateApplicationSecretRequest) -> operations.CreateApplicationSecretResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.CreateApplicationSecretRequest, base_url, '/applications/{applicationId}/secrets/', request)
        
        headers = {}
        req_content_type, data, form = utils.serialize_request_body(request, "application_secret_request", 'json')
        if req_content_type not in ('multipart/form-data', 'multipart/mixed'):
            headers['content-type'] = req_content_type
        
        client = self._client
        
        http_res = client.request('POST', url, data=data, files=form, headers=headers)
        content_type = http_res.headers.get('Content-Type')

        res = operations.CreateApplicationSecretResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ApplicationSecretResponse])
                res.application_secret_response = out

        return res

    def delete_application(self, request: operations.DeleteApplicationRequest) -> operations.DeleteApplicationResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.DeleteApplicationRequest, base_url, '/applications/{applicationId}', request)
        
        
        client = self._client
        
        http_res = client.request('DELETE', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.DeleteApplicationResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        

        return res

    def delete_application_secret(self, request: operations.DeleteApplicationSecretRequest) -> operations.DeleteApplicationSecretResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.DeleteApplicationSecretRequest, base_url, '/applications/{applicationId}/secrets/{secretId}/', request)
        
        
        client = self._client
        
        http_res = client.request('DELETE', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.DeleteApplicationSecretResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        

        return res

    def get_application(self, request: operations.GetApplicationRequest) -> operations.GetApplicationResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.GetApplicationRequest, base_url, '/applications/{applicationId}', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.GetApplicationResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ApplicationResponse])
                res.application_response = out

        return res

    def list_application_secrets(self, request: operations.ListApplicationSecretsRequest) -> operations.ListApplicationSecretsResponse:
        base_url = self._server_url
        
        url = utils.generate_url(operations.ListApplicationSecretsRequest, base_url, '/applications/{applicationId}/secrets/', request)
        
        
        client = self._client
        
        http_res = client.request('GET', url)
        content_type = http_res.headers.get('Content-Type')

        res = operations.ListApplicationSecretsResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ApplicationSecretsResponse])
                res.application_secrets_response = out

        return res

    def list_applications(self, request: operations.ListApplicationsRequest) -> operations.ListApplicationsResponse:
        base_url = self._server_url
        
        url = base_url.removesuffix('/') + '/applications/'
        
        query_params = utils.get_query_params(operations.ListApplicationsRequest, request)
        
        client = self._client
        
        http_res = client.request('GET', url, params=query_params)
        content_type = http_res.headers.get('Content-Type')

        res = operations.ListApplicationsResponse(status_code=http_res.status_code, content_type=content_type, raw_response=http_res)
        
        if http_res.status_code == 200:
            if utils.match_content_type(content_type, 'application/json'):
                out = utils.unmarshal_json(http_res.text, Optional[shared.ApplicationsResponse])
                res.applications_response = out

        return res

    