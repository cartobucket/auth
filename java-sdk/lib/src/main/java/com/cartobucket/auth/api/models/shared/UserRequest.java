/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest {
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("authorizationServerId")
    public String authorizationServerId;
    public UserRequest withAuthorizationServerId(String authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("email")
    public String email;
    public UserRequest withEmail(String email) {
        this.email = email;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("profile")
    public java.util.Map<String, Object> profile;
    public UserRequest withProfile(java.util.Map<String, Object> profile) {
        this.profile = profile;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("username")
    public String username;
    public UserRequest withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public UserRequest(){}
}
