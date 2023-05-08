/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationServerRequest {
    @JsonProperty("audience")
    public String audience;
    public AuthorizationServerRequest withAudience(String audience) {
        this.audience = audience;
        return this;
    }
    
    @JsonProperty("authorizationCodeTokenExpiration")
    public Integer authorizationCodeTokenExpiration;
    public AuthorizationServerRequest withAuthorizationCodeTokenExpiration(Integer authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
        return this;
    }
    
    @JsonProperty("clientCredentialsTokenExpiration")
    public Integer clientCredentialsTokenExpiration;
    public AuthorizationServerRequest withClientCredentialsTokenExpiration(Integer clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
        return this;
    }
    
    @JsonProperty("name")
    public String name;
    public AuthorizationServerRequest withName(String name) {
        this.name = name;
        return this;
    }
    
    @JsonProperty("serverUrl")
    public String serverUrl;
    public AuthorizationServerRequest withServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }
    
    public AuthorizationServerRequest(@JsonProperty("audience") String audience, @JsonProperty("authorizationCodeTokenExpiration") Integer authorizationCodeTokenExpiration, @JsonProperty("clientCredentialsTokenExpiration") Integer clientCredentialsTokenExpiration, @JsonProperty("name") String name, @JsonProperty("serverUrl") String serverUrl) {
        this.audience = audience;
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
        this.name = name;
        this.serverUrl = serverUrl;
  }
}