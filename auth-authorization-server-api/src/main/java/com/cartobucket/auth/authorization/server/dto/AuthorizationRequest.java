package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;

@JsonbNillable(false)
public class AuthorizationRequest {
    
    @JsonbProperty("client_id")
    @NotNull
    private String clientId;
    
    @JsonbProperty("response_type")
    @NotNull
    private String responseType;
    
    @JsonbProperty("redirect_uri")
    private String redirectUri;
    
    @JsonbProperty("scope")
    private String scope;
    
    @JsonbProperty("state")
    private String state;
    
    @JsonbProperty("nonce")
    private String nonce;
    
    @JsonbProperty("code_challenge")
    private String codeChallenge;
    
    @JsonbProperty("code_challenge_method")
    private String codeChallengeMethod;
    
    @JsonbProperty("username")
    private String username;
    
    @JsonbProperty("password")
    private String password;
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getResponseType() {
        return responseType;
    }
    
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getNonce() {
        return nonce;
    }
    
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    
    public String getCodeChallenge() {
        return codeChallenge;
    }
    
    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }
    
    public String getCodeChallengeMethod() {
        return codeChallengeMethod;
    }
    
    public void setCodeChallengeMethod(String codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}