/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.http.HttpResponse;

public class ListApplicationSecretsResponse {
    /**
     * List the secrets
     */
    
    public com.cartobucket.auth.api.models.shared.ApplicationSecretsResponse applicationSecretsResponse;
    public ListApplicationSecretsResponse withApplicationSecretsResponse(com.cartobucket.auth.api.models.shared.ApplicationSecretsResponse applicationSecretsResponse) {
        this.applicationSecretsResponse = applicationSecretsResponse;
        return this;
    }
    
    
    public String contentType;
    public ListApplicationSecretsResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    
    public Integer statusCode;
    public ListApplicationSecretsResponse withStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    
    public HttpResponse<byte[]> rawResponse;
    public ListApplicationSecretsResponse withRawResponse(HttpResponse<byte[]> rawResponse) {
        this.rawResponse = rawResponse;
        return this;
    }
    
    public ListApplicationSecretsResponse(@JsonProperty("ContentType") String contentType, @JsonProperty("StatusCode") Integer statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
  }
}
