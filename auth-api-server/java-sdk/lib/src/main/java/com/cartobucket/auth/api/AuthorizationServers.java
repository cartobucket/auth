/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api;

import com.cartobucket.auth.api.utils.HTTPClient;
import com.cartobucket.auth.api.utils.HTTPRequest;
import com.cartobucket.auth.api.utils.JSON;
import com.cartobucket.auth.api.utils.SerializedBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * An AuthorizationServer is the top level domain model that grants AccessTokens.
 */
public class AuthorizationServers {
	
	private HTTPClient _defaultClient;
	private HTTPClient _securityClient;
	private String _serverUrl;
	private String _language;
	private String _sdkVersion;
	private String _genVersion;

	public AuthorizationServers(HTTPClient defaultClient, HTTPClient securityClient, String serverUrl, String language, String sdkVersion, String genVersion) {
		this._defaultClient = defaultClient;
		this._securityClient = securityClient;
		this._serverUrl = serverUrl;
		this._language = language;
		this._sdkVersion = sdkVersion;
		this._genVersion = genVersion;
	}

    public com.cartobucket.auth.api.models.operations.CreateAuthorizationServerResponse createAuthorizationServer(com.cartobucket.auth.api.models.shared.AuthorizationServerRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/authorizationServers/");
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("POST");
        req.setURL(url);
        SerializedBody serializedRequestBody = com.cartobucket.auth.api.utils.Utils.serializeRequestBody(request, "request", "json");
        if (serializedRequestBody == null) {
            throw new Exception("Request body is required");
        }
        req.setBody(serializedRequestBody);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.CreateAuthorizationServerResponse res = new com.cartobucket.auth.api.models.operations.CreateAuthorizationServerResponse(contentType, httpRes.statusCode()) {{
            authorizationServerResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.AuthorizationServerResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.AuthorizationServerResponse.class);
                res.authorizationServerResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerResponse deleteAuthorizationServer(com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerRequest.class, baseUrl, "/authorizationServers/{authorizationServerId}", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("DELETE");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerResponse res = new com.cartobucket.auth.api.models.operations.DeleteAuthorizationServerResponse(contentType, httpRes.statusCode()) {{
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 204) {
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.GetAuthorizationServerResponse getAuthorizationServer(com.cartobucket.auth.api.models.operations.GetAuthorizationServerRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.GetAuthorizationServerRequest.class, baseUrl, "/authorizationServers/{authorizationServerId}", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.GetAuthorizationServerResponse res = new com.cartobucket.auth.api.models.operations.GetAuthorizationServerResponse(contentType, httpRes.statusCode()) {{
            authorizationServerResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.AuthorizationServerResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.AuthorizationServerResponse.class);
                res.authorizationServerResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.ListAuthorizationServersResponse listAuthorizationServers() throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/authorizationServers/");
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.ListAuthorizationServersResponse res = new com.cartobucket.auth.api.models.operations.ListAuthorizationServersResponse(contentType, httpRes.statusCode()) {{
            authorizationServersResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.AuthorizationServersResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.AuthorizationServersResponse.class);
                res.authorizationServersResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerResponse updateAuthorizationServer(com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerRequest.class, baseUrl, "/authorizationServers/{authorizationServerId}", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("PUT");
        req.setURL(url);
        SerializedBody serializedRequestBody = com.cartobucket.auth.api.utils.Utils.serializeRequestBody(request, "authorizationServerRequest", "json");
        if (serializedRequestBody == null) {
            throw new Exception("Request body is required");
        }
        req.setBody(serializedRequestBody);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerResponse res = new com.cartobucket.auth.api.models.operations.UpdateAuthorizationServerResponse(contentType, httpRes.statusCode()) {{
            authorizationServerResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.AuthorizationServerResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.AuthorizationServerResponse.class);
                res.authorizationServerResponse = out;
            }
        }

        return res;
    }
}