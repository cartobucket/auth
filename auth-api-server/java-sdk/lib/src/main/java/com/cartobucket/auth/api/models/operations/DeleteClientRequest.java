/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteClientRequest {
    /**
     * The clientId of the resource.
     */
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=clientId")
    public String clientId;
    public DeleteClientRequest withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
    
    public DeleteClientRequest(@JsonProperty("clientId") String clientId) {
        this.clientId = clientId;
  }
}
