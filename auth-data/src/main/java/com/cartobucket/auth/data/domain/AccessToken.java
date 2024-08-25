/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.data.domain;


import java.util.Objects;

/**
 *
 **/

public class AccessToken {
    private String accessToken;

    private TokenTypeEnum tokenType;
    private String refreshToken;
    private Integer expiresIn;
    private String idToken;
    private String scope;

    public AccessToken accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken tokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
        return this;
    }


    public TokenTypeEnum getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenTypeEnum tokenType) {
        this.tokenType = tokenType;
    }

    public AccessToken refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AccessToken expiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }


    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public AccessToken idToken(String idToken) {
        this.idToken = idToken;
        return this;
    }


    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public AccessToken scope(String scope) {
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
        AccessToken accessTokenResponse = (AccessToken) o;
        return Objects.equals(this.accessToken, accessTokenResponse.accessToken) &&
                Objects.equals(this.tokenType, accessTokenResponse.tokenType) &&
                Objects.equals(this.refreshToken, accessTokenResponse.refreshToken) &&
                Objects.equals(this.expiresIn, accessTokenResponse.expiresIn) &&
                Objects.equals(this.idToken, accessTokenResponse.idToken) &&
                Objects.equals(this.scope, accessTokenResponse.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, refreshToken, expiresIn, idToken, scope);
    }

    @Override
    public String toString() {

        String sb = "class AccessTokenResponse {\n" +
                "    accessToken: " + toIndentedString(accessToken) + "\n" +
                "    tokenType: " + toIndentedString(tokenType) + "\n" +
                "    refreshToken: " + toIndentedString(refreshToken) + "\n" +
                "    expiresIn: " + toIndentedString(expiresIn) + "\n" +
                "    idToken: " + toIndentedString(idToken) + "\n" +
                "    scope: " + toIndentedString(scope) + "\n" +
                "}";
        return sb;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
