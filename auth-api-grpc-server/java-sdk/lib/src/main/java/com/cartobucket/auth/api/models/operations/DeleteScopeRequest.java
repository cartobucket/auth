/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteScopeRequest {
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=scopeId")
    public String scopeId;
    public DeleteScopeRequest withScopeId(String scopeId) {
        this.scopeId = scopeId;
        return this;
    }
    
    public DeleteScopeRequest(@JsonProperty("scopeId") String scopeId) {
        this.scopeId = scopeId;
  }
}