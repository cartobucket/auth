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
import java.time.OffsetDateTime;
import org.apache.http.NameValuePair;

/**
 * Scopes are a set of tags used to indicate Authorization. Currently only static Scopes are supported.
 */
public class Scopes {
	
	private HTTPClient _defaultClient;
	private HTTPClient _securityClient;
	private String _serverUrl;
	private String _language;
	private String _sdkVersion;
	private String _genVersion;

	public Scopes(HTTPClient defaultClient, HTTPClient securityClient, String serverUrl, String language, String sdkVersion, String genVersion) {
		this._defaultClient = defaultClient;
		this._securityClient = securityClient;
		this._serverUrl = serverUrl;
		this._language = language;
		this._sdkVersion = sdkVersion;
		this._genVersion = genVersion;
	}

    public com.cartobucket.auth.api.models.operations.CreateScopeResponse createScope(com.cartobucket.auth.api.models.shared.ScopeRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/scopes/");
        
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

        com.cartobucket.auth.api.models.operations.CreateScopeResponse res = new com.cartobucket.auth.api.models.operations.CreateScopeResponse(contentType, httpRes.statusCode()) {{
            scopeResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.ScopeResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.ScopeResponse.class);
                res.scopeResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.DeleteScopeResponse deleteScope(com.cartobucket.auth.api.models.operations.DeleteScopeRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.DeleteScopeRequest.class, baseUrl, "/scopes/{scopeId}/", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("DELETE");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.DeleteScopeResponse res = new com.cartobucket.auth.api.models.operations.DeleteScopeResponse(contentType, httpRes.statusCode()) {{
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.GetScopeResponse getScope(com.cartobucket.auth.api.models.operations.GetScopeRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.GetScopeRequest.class, baseUrl, "/scopes/{scopeId}/", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.GetScopeResponse res = new com.cartobucket.auth.api.models.operations.GetScopeResponse(contentType, httpRes.statusCode()) {{
            scopeResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.ScopeResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.ScopeResponse.class);
                res.scopeResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.ListScopesResponse listScopes(com.cartobucket.auth.api.models.operations.ListScopesRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/scopes/");
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        java.util.List<NameValuePair> queryParams = com.cartobucket.auth.api.utils.Utils.getQueryParams(com.cartobucket.auth.api.models.operations.ListScopesRequest.class, request, null);
        if (queryParams != null) {
            for (NameValuePair queryParam : queryParams) {
                req.addQueryParam(queryParam);
            }
        }
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.ListScopesResponse res = new com.cartobucket.auth.api.models.operations.ListScopesResponse(contentType, httpRes.statusCode()) {{
            scopesResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.ScopesResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.ScopesResponse.class);
                res.scopesResponse = out;
            }
        }

        return res;
    }
}