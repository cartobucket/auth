/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserRequest {
    @SpeakeasyMetadata("request:mediaType=application/json")
    public com.cartobucket.auth.api.models.shared.UserRequest userRequest;
    public UpdateUserRequest withUserRequest(com.cartobucket.auth.api.models.shared.UserRequest userRequest) {
        this.userRequest = userRequest;
        return this;
    }
    
    @SpeakeasyMetadata("pathParam:style=simple,explode=false,name=userId")
    public String userId;
    public UpdateUserRequest withUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
    public UpdateUserRequest(@JsonProperty("UserRequest") com.cartobucket.auth.api.models.shared.UserRequest userRequest, @JsonProperty("userId") String userId) {
        this.userRequest = userRequest;
        this.userId = userId;
  }
}
