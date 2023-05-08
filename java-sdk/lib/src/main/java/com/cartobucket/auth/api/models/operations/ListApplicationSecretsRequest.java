/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListApplicationSecretsRequest {
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=applicationId")
    public String applicationId;
    public ListApplicationSecretsRequest withApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }
    
    public ListApplicationSecretsRequest(@JsonProperty("applicationId") String applicationId) {
        this.applicationId = applicationId;
  }
}