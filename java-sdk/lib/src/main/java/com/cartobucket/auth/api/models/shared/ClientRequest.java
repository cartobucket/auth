/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientRequest {
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("authorizationServerId")
    public String authorizationServerId;
    public ClientRequest withAuthorizationServerId(String authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("name")
    public String name;
    public ClientRequest withName(String name) {
        this.name = name;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("redirectUris")
    public String[] redirectUris;
    public ClientRequest withRedirectUris(String[] redirectUris) {
        this.redirectUris = redirectUris;
        return this;
    }
    
    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("scopes")
    public String scopes;
    public ClientRequest withScopes(String scopes) {
        this.scopes = scopes;
        return this;
    }
    
    public ClientRequest(){}
}
