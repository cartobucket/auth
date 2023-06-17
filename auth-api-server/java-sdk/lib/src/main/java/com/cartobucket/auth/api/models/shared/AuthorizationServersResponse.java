/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AuthorizationServersResponse - Get a list of the authorization servers.
 */
public class AuthorizationServersResponse {
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("authorizationServers")
    public AuthorizationServerResponse[] authorizationServers;
    public AuthorizationServersResponse withAuthorizationServers(AuthorizationServerResponse[] authorizationServers) {
        this.authorizationServers = authorizationServers;
        return this;
    }
    
    public AuthorizationServersResponse(){}
}