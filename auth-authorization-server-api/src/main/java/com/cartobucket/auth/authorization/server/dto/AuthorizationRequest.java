package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Authorization request parameters")
@JsonbNillable(false)
public class AuthorizationRequest {
    
    @JsonbProperty("client_id")
    @Schema(description = "Client identifier", required = true)
    @NotNull
    private String clientId;
    
    @JsonbProperty("response_type")
    @Schema(description = "Response type", required = true, allowableValues = {"code", "token", "id_token"})
    @NotNull
    private String responseType;
    
    @JsonbProperty("redirect_uri")
    @Schema(description = "Redirect URI")
    private String redirectUri;
    
    @JsonbProperty("scope")
    @Schema(description = "Requested scopes")
    private String scope;
    
    @JsonbProperty("state")
    @Schema(description = "State parameter")
    private String state;
    
    @JsonbProperty("nonce")
    @Schema(description = "Nonce for ID token")
    private String nonce;
    
    @JsonbProperty("code_challenge")
    @Schema(description = "PKCE code challenge")
    private String codeChallenge;
    
    @JsonbProperty("code_challenge_method")
    @Schema(description = "PKCE code challenge method", allowableValues = {"plain", "S256"})
    private String codeChallengeMethod;
    
    @JsonbProperty("username")
    @Schema(description = "Username for authentication")
    private String username;
    
    @JsonbProperty("password")
    @Schema(description = "Password for authentication")
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