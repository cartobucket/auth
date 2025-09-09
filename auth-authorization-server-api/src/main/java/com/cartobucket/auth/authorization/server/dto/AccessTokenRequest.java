package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.util.Objects;

@JsonbNillable(false)
public class AccessTokenRequest {
    
    @JsonbTypeAdapter(GrantTypeAdapter.class)
    public enum GrantTypeEnum {
        CLIENT_CREDENTIALS("client_credentials"), 
        AUTHORIZATION_CODE("authorization_code"), 
        REFRESH_TOKEN("refresh_token");

        private String value;

        GrantTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static GrantTypeEnum fromString(String s) {
            for (GrantTypeEnum b : GrantTypeEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static GrantTypeEnum fromValue(String value) {
            for (GrantTypeEnum b : GrantTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
    
    public static class GrantTypeAdapter implements JsonbAdapter<GrantTypeEnum, String> {
        @Override
        public String adaptToJson(GrantTypeEnum value) {
            return value != null ? value.value() : null;
        }
        
        @Override
        public GrantTypeEnum adaptFromJson(String value) {
            return value != null ? GrantTypeEnum.fromValue(value) : null;
        }
    }
    
    @JsonbProperty("client_id")
    @NotNull
    private @Valid String clientId;
    
    @JsonbProperty("client_secret")
    private @Valid String clientSecret;
    
    @JsonbProperty("grant_type")
    @NotNull
    private @Valid GrantTypeEnum grantType;
    
    @JsonbProperty("code")
    private @Valid String code;
    
    @JsonbProperty("redirect_uri")
    private @Valid String redirectUri;
    
    @JsonbProperty("code_verifier")
    private @Valid String codeVerifier;
    
    @JsonbProperty("refresh_token")
    private @Valid String refreshToken;
    
    @JsonbProperty("scope")
    private @Valid String scope;
    
    public AccessTokenRequest clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public AccessTokenRequest clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public AccessTokenRequest grantType(GrantTypeEnum grantType) {
        this.grantType = grantType;
        return this;
    }
    
    public GrantTypeEnum getGrantType() {
        return grantType;
    }
    
    public void setGrantType(GrantTypeEnum grantType) {
        this.grantType = grantType;
    }
    
    public AccessTokenRequest code(String code) {
        this.code = code;
        return this;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public AccessTokenRequest redirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public AccessTokenRequest codeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
        return this;
    }
    
    public String getCodeVerifier() {
        return codeVerifier;
    }
    
    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }
    
    public AccessTokenRequest refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public AccessTokenRequest scope(String scope) {
        this.scope = scope;
        return this;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenRequest accessTokenRequest = (AccessTokenRequest) o;
        return Objects.equals(this.clientId, accessTokenRequest.clientId) &&
            Objects.equals(this.clientSecret, accessTokenRequest.clientSecret) &&
            Objects.equals(this.grantType, accessTokenRequest.grantType) &&
            Objects.equals(this.refreshToken, accessTokenRequest.refreshToken) &&
            Objects.equals(this.code, accessTokenRequest.code) &&
            Objects.equals(this.redirectUri, accessTokenRequest.redirectUri) &&
            Objects.equals(this.codeVerifier, accessTokenRequest.codeVerifier) &&
            Objects.equals(this.scope, accessTokenRequest.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret, grantType, refreshToken, code, redirectUri, codeVerifier, scope);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccessTokenRequest {\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
        sb.append("    grantType: ").append(toIndentedString(grantType)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    redirectUri: ").append(toIndentedString(redirectUri)).append("\n");
        sb.append("    codeVerifier: ").append(toIndentedString(codeVerifier)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}