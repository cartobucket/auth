/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetOpenIdConnectionWellKnownRequest {
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=authorizationServerId")
    public String authorizationServerId;
    public GetOpenIdConnectionWellKnownRequest withAuthorizationServerId(String authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }
    
    public GetOpenIdConnectionWellKnownRequest(@JsonProperty("authorizationServerId") String authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
  }
}