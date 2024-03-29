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
 * Templates are used to render information such as the login page.
 */
public class Templates {
	
	private HTTPClient _defaultClient;
	private HTTPClient _securityClient;
	private String _serverUrl;
	private String _language;
	private String _sdkVersion;
	private String _genVersion;

	public Templates(HTTPClient defaultClient, HTTPClient securityClient, String serverUrl, String language, String sdkVersion, String genVersion) {
		this._defaultClient = defaultClient;
		this._securityClient = securityClient;
		this._serverUrl = serverUrl;
		this._language = language;
		this._sdkVersion = sdkVersion;
		this._genVersion = genVersion;
	}

    public com.cartobucket.auth.api.models.operations.CreateTemplateResponse createTemplate(com.cartobucket.auth.api.models.shared.TemplateRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/templates/");
        
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

        com.cartobucket.auth.api.models.operations.CreateTemplateResponse res = new com.cartobucket.auth.api.models.operations.CreateTemplateResponse(contentType, httpRes.statusCode()) {{
            templateResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.TemplateResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.TemplateResponse.class);
                res.templateResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.DeleteTemplateResponse deleteTemplate(com.cartobucket.auth.api.models.operations.DeleteTemplateRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.DeleteTemplateRequest.class, baseUrl, "/templates/{templateId}/", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("DELETE");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.DeleteTemplateResponse res = new com.cartobucket.auth.api.models.operations.DeleteTemplateResponse(contentType, httpRes.statusCode()) {{
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.GetTemplateResponse getTemplate(com.cartobucket.auth.api.models.operations.GetTemplateRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.GetTemplateRequest.class, baseUrl, "/templates/{templateId}/", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.GetTemplateResponse res = new com.cartobucket.auth.api.models.operations.GetTemplateResponse(contentType, httpRes.statusCode()) {{
            templateResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.TemplateResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.TemplateResponse.class);
                res.templateResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.ListTemplatesResponse listTemplates(com.cartobucket.auth.api.models.operations.ListTemplatesRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(baseUrl, "/templates/");
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("GET");
        req.setURL(url);
        
        java.util.List<NameValuePair> queryParams = com.cartobucket.auth.api.utils.Utils.getQueryParams(com.cartobucket.auth.api.models.operations.ListTemplatesRequest.class, request, null);
        if (queryParams != null) {
            for (NameValuePair queryParam : queryParams) {
                req.addQueryParam(queryParam);
            }
        }
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.ListTemplatesResponse res = new com.cartobucket.auth.api.models.operations.ListTemplatesResponse(contentType, httpRes.statusCode()) {{
            templateResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.TemplateResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.TemplateResponse.class);
                res.templateResponse = out;
            }
        }

        return res;
    }

    public com.cartobucket.auth.api.models.operations.UpdateTemplateResponse updateTemplate(com.cartobucket.auth.api.models.operations.UpdateTemplateRequest request) throws Exception {
        String baseUrl = this._serverUrl;
        String url = com.cartobucket.auth.api.utils.Utils.generateURL(com.cartobucket.auth.api.models.operations.UpdateTemplateRequest.class, baseUrl, "/templates/{templateId}/", request, null);
        
        HTTPRequest req = new HTTPRequest();
        req.setMethod("PUT");
        req.setURL(url);
        SerializedBody serializedRequestBody = com.cartobucket.auth.api.utils.Utils.serializeRequestBody(request, "templateRequest", "json");
        if (serializedRequestBody == null) {
            throw new Exception("Request body is required");
        }
        req.setBody(serializedRequestBody);
        
        
        HTTPClient client = this._defaultClient;
        HttpResponse<byte[]> httpRes = client.send(req);

        String contentType = httpRes.headers().firstValue("Content-Type").orElse("application/octet-stream");

        com.cartobucket.auth.api.models.operations.UpdateTemplateResponse res = new com.cartobucket.auth.api.models.operations.UpdateTemplateResponse(contentType, httpRes.statusCode()) {{
            templateResponse = null;
        }};
        res.rawResponse = httpRes;
        
        if (httpRes.statusCode() == 200) {
            if (com.cartobucket.auth.api.utils.Utils.matchContentType(contentType, "application/json")) {
                ObjectMapper mapper = JSON.getMapper();
                com.cartobucket.auth.api.models.shared.TemplateResponse out = mapper.readValue(new String(httpRes.body(), StandardCharsets.UTF_8), com.cartobucket.auth.api.models.shared.TemplateResponse.class);
                res.templateResponse = out;
            }
        }

        return res;
    }
}