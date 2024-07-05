"""Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT."""

import requests as requests_http
from . import utils
from .applications import Applications
from .authorizationserver import AuthorizationServer
from .authorizationservers import AuthorizationServers
from .clients import Clients
from .scopes import Scopes
from .templates import Templates
from .users import Users

SERVERS = [
    "https://auth.cartobucket.com",
]
"""Contains the list of servers available to the SDK"""

class SDK:
    r"""OAuth 2.1/OIDC Server."""
    applications: Applications
    r"""Applications are used for Server to Server communication. Each Application has a set of Scopes. An ApplicationSecret can be created for each Server and the ApplicationSecret can have the same set of Scopes as the Application, or a subset."""
    authorization_server: AuthorizationServer
    r"""This is where the interaction with the Authorization Server occurs."""
    authorization_servers: AuthorizationServers
    r"""An AuthorizationServer is the top level domain model that grants AccessTokens."""
    clients: Clients
    r"""Clients are used to initiate and Authorization Code Flow. Currently only Username/Password logins are available."""
    scopes: Scopes
    r"""Scopes are a set of tags used to indicate Authorization. Currently only static Scopes are supported."""
    templates: Templates
    r"""Templates are used to render information such as the login page."""
    users: Users
    r"""Users represent a person entity in the system. The User domain model is used in the Authorization Code Flow and can be granted Access Tokens through a Client."""

    _client: requests_http.Session
    _security_client: requests_http.Session
    _server_url: str = SERVERS[0]
    _language: str = "python"
    _sdk_version: str = "0.0.1"
    _gen_version: str = "2.23.4"

    def __init__(self,
                 server_url: str = None,
                 url_params: dict[str, str] = None,
                 client: requests_http.Session = None
                 ) -> None:
        """Instantiates the SDK configuring it with the provided parameters.
        
        :param server_url: The server URL to use for all operations
        :type server_url: str
        :param url_params: Parameters to optionally template the server URL with
        :type url_params: dict[str, str]
        :param client: The requests.Session HTTP client to use for all operations
        :type client: requests_http.Session        
        """
        self._client = requests_http.Session()
        
        
        if server_url is not None:
            if url_params is not None:
                self._server_url = utils.template_url(server_url, url_params)
            else:
                self._server_url = server_url

        if client is not None:
            self._client = client
        
        self._security_client = self._client
        

        self._init_sdks()
    
    def _init_sdks(self):
        self.applications = Applications(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.authorization_server = AuthorizationServer(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.authorization_servers = AuthorizationServers(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.clients = Clients(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.scopes = Scopes(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.templates = Templates(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
        self.users = Users(
            self._client,
            self._security_client,
            self._server_url,
            self._language,
            self._sdk_version,
            self._gen_version
        )
        
    