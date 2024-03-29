/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UsersResponse - Get a list of User.
 */
public class UsersResponse {
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("users")
    public UserResponse[] users;
    public UsersResponse withUsers(UserResponse[] users) {
        this.users = users;
        return this;
    }
    
    public UsersResponse(){}
}
