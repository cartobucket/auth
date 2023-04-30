/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.http.HttpResponse;

public class GetAuthorizationServerResponse {
    /**
     * Get an Authorization Server.
     */
    
    public com.cartobucket.auth.api.models.shared.AuthorizationServerResponse authorizationServerResponse;
    public GetAuthorizationServerResponse withAuthorizationServerResponse(com.cartobucket.auth.api.models.shared.AuthorizationServerResponse authorizationServerResponse) {
        this.authorizationServerResponse = authorizationServerResponse;
        return this;
    }
    
    
    public String contentType;
    public GetAuthorizationServerResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    
    public Integer statusCode;
    public GetAuthorizationServerResponse withStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    
    public HttpResponse<byte[]> rawResponse;
    public GetAuthorizationServerResponse withRawResponse(HttpResponse<byte[]> rawResponse) {
        this.rawResponse = rawResponse;
        return this;
    }
    
    public GetAuthorizationServerResponse(@JsonProperty("ContentType") String contentType, @JsonProperty("StatusCode") Integer statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
  }
}
