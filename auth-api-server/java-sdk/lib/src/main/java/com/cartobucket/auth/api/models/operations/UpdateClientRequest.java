/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateClientRequest {
    @SpeakeasyMetadata("request:mediaType=application/json")
    public com.cartobucket.auth.api.models.shared.ClientRequest clientRequest;
    public UpdateClientRequest withClientRequest(com.cartobucket.auth.api.models.shared.ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
        return this;
    }
    
    /**
     * The clientId of the resource.
     */
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=clientId")
    public String clientId;
    public UpdateClientRequest withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
    
    public UpdateClientRequest(@JsonProperty("ClientRequest") com.cartobucket.auth.api.models.shared.ClientRequest clientRequest, @JsonProperty("clientId") String clientId) {
        this.clientRequest = clientRequest;
        this.clientId = clientId;
  }
}
