
package com.cartobucket.auth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;

import java.util.Objects;

/**
* 
**/
@ApiModel(description = "")
@JsonTypeName("AccessTokenResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2023-05-29T17:50:42.716026387-07:00[America/Los_Angeles]")
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)

public class AccessToken {
    private @Valid String accessToken;
            public enum TokenTypeEnum {

    BEARER(String.valueOf("Bearer"));


    private String value;

    TokenTypeEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static TokenTypeEnum fromString(String s) {
        for (TokenTypeEnum b : TokenTypeEnum.values()) {
            // using Objects.toString() to be safe if value type non-object type
            // because types like 'int' etc. will be auto-boxed
            if (Objects.toString(b.value).equals(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected string value '" + s + "'");
	}
	
    @JsonCreator
    public static TokenTypeEnum fromValue(String value) {
        for (TokenTypeEnum b : TokenTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private @Valid TokenTypeEnum tokenType;
    private @Valid String refreshToken;
    private @Valid Integer expiresIn;
    private @Valid String idToken;
    private @Valid String scope;

    /**
    **/
    public AccessToken accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("access_token")
      public String getAccessToken() {
    return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    }

    /**
    **/
    public AccessToken tokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("token_type")
      public TokenTypeEnum getTokenType() {
    return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
    }

    /**
    **/
    public AccessToken refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("refresh_token")
      public String getRefreshToken() {
    return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    }

    /**
    **/
    public AccessToken expiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("expires_in")
      public Integer getExpiresIn() {
    return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
    }

    /**
    **/
    public AccessToken idToken(String idToken) {
    this.idToken = idToken;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("id_token")
      public String getIdToken() {
    return idToken;
    }

    @JsonProperty("id_token")
    public void setIdToken(String idToken) {
    this.idToken = idToken;
    }

    /**
        * 
    **/
    public AccessToken scope(String scope) {
    this.scope = scope;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonProperty("scope")
      public String getScope() {
    return scope;
    }

    @JsonProperty("scope")
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
StringBuilder sb = new StringBuilder();
sb.append("class AccessTokenResponse {\n");

sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n");
sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
sb.append("}");
return sb.toString();
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
