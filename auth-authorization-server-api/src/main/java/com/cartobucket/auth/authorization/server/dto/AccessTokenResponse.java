package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.validation.Valid;
import java.util.Objects;

@JsonbPropertyOrder({"access_token", "token_type", "expires_in", "refresh_token", "scope", "id_token"})
public class AccessTokenResponse {
    
    @JsonbTypeAdapter(TokenTypeAdapter.class)
    public enum TokenTypeEnum {
        BEARER("Bearer");

        private String value;

        TokenTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TokenTypeEnum fromString(String s) {
            for (TokenTypeEnum b : TokenTypeEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static TokenTypeEnum fromValue(String value) {
            for (TokenTypeEnum b : TokenTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
    
    public static class TokenTypeAdapter implements JsonbAdapter<TokenTypeEnum, String> {
        @Override
        public String adaptToJson(TokenTypeEnum value) {
            return value != null ? value.value() : null;
        }
        
        @Override
        public TokenTypeEnum adaptFromJson(String value) {
            return value != null ? TokenTypeEnum.fromValue(value) : null;
        }
    }
    
    @JsonbProperty("access_token")
    private @Valid String accessToken;
    
    @JsonbProperty("token_type")
    private @Valid TokenTypeEnum tokenType;
    
    @JsonbProperty("expires_in")
    private @Valid Integer expiresIn;
    
    @JsonbProperty("refresh_token")
    private @Valid String refreshToken;
    
    @JsonbProperty("scope")
    private @Valid String scope;
    
    @JsonbProperty("id_token")
    private @Valid String idToken;
    
    public AccessTokenResponse accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public AccessTokenResponse tokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
        return this;
    }
    
    public TokenTypeEnum getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
    }
    
    public AccessTokenResponse expiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
    
    public Integer getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public AccessTokenResponse refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public AccessTokenResponse scope(String scope) {
        this.scope = scope;
        return this;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public AccessTokenResponse idToken(String idToken) {
        this.idToken = idToken;
        return this;
    }
    
    public String getIdToken() {
        return idToken;
    }
    
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenResponse accessTokenResponse = (AccessTokenResponse) o;
        return Objects.equals(this.accessToken, accessTokenResponse.accessToken) &&
            Objects.equals(this.tokenType, accessTokenResponse.tokenType) &&
            Objects.equals(this.expiresIn, accessTokenResponse.expiresIn) &&
            Objects.equals(this.refreshToken, accessTokenResponse.refreshToken) &&
            Objects.equals(this.scope, accessTokenResponse.scope) &&
            Objects.equals(this.idToken, accessTokenResponse.idToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, expiresIn, refreshToken, scope, idToken);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccessTokenResponse {\n");
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n");
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