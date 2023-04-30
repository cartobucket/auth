/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.http.HttpResponse;

public class ListClientsResponse {
    /**
     * Gets a list of clients.
     */
    
    public com.cartobucket.auth.api.models.shared.ClientsResponse clientsResponse;
    public ListClientsResponse withClientsResponse(com.cartobucket.auth.api.models.shared.ClientsResponse clientsResponse) {
        this.clientsResponse = clientsResponse;
        return this;
    }
    
    
    public String contentType;
    public ListClientsResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    
    public Integer statusCode;
    public ListClientsResponse withStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    
    
    public HttpResponse<byte[]> rawResponse;
    public ListClientsResponse withRawResponse(HttpResponse<byte[]> rawResponse) {
        this.rawResponse = rawResponse;
        return this;
    }
    
    public ListClientsResponse(@JsonProperty("ContentType") String contentType, @JsonProperty("StatusCode") Integer statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
  }
}
