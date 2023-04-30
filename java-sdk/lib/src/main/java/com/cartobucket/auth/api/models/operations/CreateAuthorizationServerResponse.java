/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.http.HttpResponse;

public class CreateAuthorizationServerResponse {
    /**
     * Create a new Authorization Server.
     */
    
    public com.cartobucket.auth.api.models.shared.AuthorizationServerResponse authorizationServerResponse;
    public CreateAuthorizationServerResponse withAuthorizationServerResponse(com.cartobucket.auth.api.models.shared.AuthorizationServerResponse authorizationServerResponse) {
        this.authorizationServerResponse = authorizationServerResponse;
        return this;
    }
    
    
    public String contentType;
    public CreateAuthorizationServerResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    
    public Integer statusCode;
    public CreateAuthorizationServerResponse withStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    
    public HttpResponse<byte[]> rawResponse;
    public CreateAuthorizationServerResponse withRawResponse(HttpResponse<byte[]> rawResponse) {
        this.rawResponse = rawResponse;
        return this;
    }
    
    public CreateAuthorizationServerResponse(@JsonProperty("ContentType") String contentType, @JsonProperty("StatusCode") Integer statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
  }
}
