package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class WellKnown {
    
    public enum ResponseTypesSupportedEnum {
        CODE("code"), 
        TOKEN("token"), 
        CODE_ID_TOKEN("code id_token");

        private String value;

        ResponseTypesSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ResponseTypesSupportedEnum fromString(String s) {
            for (ResponseTypesSupportedEnum b : ResponseTypesSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static ResponseTypesSupportedEnum fromValue(String value) {
            for (ResponseTypesSupportedEnum b : ResponseTypesSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum SubjectTypesSupportedEnum {
        PUBLIC("public");

        private String value;

        SubjectTypesSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static SubjectTypesSupportedEnum fromString(String s) {
            for (SubjectTypesSupportedEnum b : SubjectTypesSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static SubjectTypesSupportedEnum fromValue(String value) {
            for (SubjectTypesSupportedEnum b : SubjectTypesSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum IdTokenSigningAlgValuesSupportedEnum {
        RS256("RS256"), 
        RS512("RS512"), 
        EDDSA("EdDSA");

        private String value;

        IdTokenSigningAlgValuesSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static IdTokenSigningAlgValuesSupportedEnum fromString(String s) {
            for (IdTokenSigningAlgValuesSupportedEnum b : IdTokenSigningAlgValuesSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static IdTokenSigningAlgValuesSupportedEnum fromValue(String value) {
            for (IdTokenSigningAlgValuesSupportedEnum b : IdTokenSigningAlgValuesSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum ScopesSupportedEnum {
        OPENID("openid"), 
        EMAIL("email"), 
        PROFILE("profile");

        private String value;

        ScopesSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ScopesSupportedEnum fromString(String s) {
            for (ScopesSupportedEnum b : ScopesSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static ScopesSupportedEnum fromValue(String value) {
            for (ScopesSupportedEnum b : ScopesSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum TokenEndpointAuthMethodsSupportedEnum {
        POST("client_secret_post"), 
        BASIC("client_secret_basic");

        private String value;

        TokenEndpointAuthMethodsSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TokenEndpointAuthMethodsSupportedEnum fromString(String s) {
            for (TokenEndpointAuthMethodsSupportedEnum b : TokenEndpointAuthMethodsSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static TokenEndpointAuthMethodsSupportedEnum fromValue(String value) {
            for (TokenEndpointAuthMethodsSupportedEnum b : TokenEndpointAuthMethodsSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum ClaimsSupportedEnum {
        AUD("aud"), 
        EXP("exp"), 
        FAMILY_NAME("family_name"), 
        GIVEN_NAME("given_name"), 
        IAT("iat"), 
        ISS("iss"), 
        NAME("name"), 
        SUB("sub");

        private String value;

        ClaimsSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ClaimsSupportedEnum fromString(String s) {
            for (ClaimsSupportedEnum b : ClaimsSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static ClaimsSupportedEnum fromValue(String value) {
            for (ClaimsSupportedEnum b : ClaimsSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public enum CodeChallengeMethodsSupportedEnum {
        S256("S256");

        private String value;

        CodeChallengeMethodsSupportedEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static CodeChallengeMethodsSupportedEnum fromString(String s) {
            for (CodeChallengeMethodsSupportedEnum b : CodeChallengeMethodsSupportedEnum.values()) {
                if (java.util.Objects.toString(b.value).equals(s)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected string value '" + s + "'");
        }

        public static CodeChallengeMethodsSupportedEnum fromValue(String value) {
            for (CodeChallengeMethodsSupportedEnum b : CodeChallengeMethodsSupportedEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
    
    @JsonbProperty("issuer")
    private @Valid String issuer;
    
    @JsonbProperty("authorization_endpoint")
    private @Valid String authorizationEndpoint;
    
    @JsonbProperty("token_endpoint")
    private @Valid String tokenEndpoint;
    
    @JsonbProperty("userinfo_endpoint")
    private @Valid String userinfoEndpoint;
    
    @JsonbProperty("jwks_uri")
    private @Valid String jwksUri;
    
    @JsonbProperty("revocation_endpoint")
    private @Valid String revocationEndpoint;
    
    @JsonbProperty("scopes_supported")
    private @Valid List<String> scopesSupported;
    
    @JsonbProperty("response_types_supported")
    private @Valid List<String> responseTypesSupported;
    
    @JsonbProperty("grant_types_supported")
    private @Valid List<String> grantTypesSupported;
    
    @JsonbProperty("subject_types_supported")
    private @Valid List<String> subjectTypesSupported;
    
    @JsonbProperty("id_token_signing_alg_values_supported")
    private @Valid List<String> idTokenSigningAlgValuesSupported;
    
    @JsonbProperty("token_endpoint_auth_methods_supported")
    private @Valid List<String> tokenEndpointAuthMethodsSupported;
    
    @JsonbProperty("claims_supported")
    private @Valid List<String> claimsSupported;
    
    @JsonbProperty("code_challenge_methods_supported")
    private @Valid List<String> codeChallengeMethodsSupported;
    
    public WellKnown issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }
    
    public String getIssuer() {
        return issuer;
    }
    
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    
    public WellKnown authorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
        return this;
    }
    
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }
    
    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }
    
    public WellKnown tokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
        return this;
    }
    
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
    
    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }
    
    public WellKnown userinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
        return this;
    }
    
    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }
    
    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }
    
    public WellKnown jwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
        return this;
    }
    
    public String getJwksUri() {
        return jwksUri;
    }
    
    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }
    
    public WellKnown revocationEndpoint(String revocationEndpoint) {
        this.revocationEndpoint = revocationEndpoint;
        return this;
    }
    
    public String getRevocationEndpoint() {
        return revocationEndpoint;
    }
    
    public void setRevocationEndpoint(String revocationEndpoint) {
        this.revocationEndpoint = revocationEndpoint;
    }
    
    public WellKnown scopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
        return this;
    }
    
    public List<String> getScopesSupported() {
        return scopesSupported;
    }
    
    public void setScopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
    }
    
    public WellKnown responseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
        return this;
    }
    
    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }
    
    public void setResponseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }
    
    public WellKnown grantTypesSupported(List<String> grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
        return this;
    }
    
    public List<String> getGrantTypesSupported() {
        return grantTypesSupported;
    }
    
    public void setGrantTypesSupported(List<String> grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }
    
    public WellKnown subjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
        return this;
    }
    
    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }
    
    public void setSubjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }
    
    public WellKnown idTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
        return this;
    }
    
    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }
    
    public void setIdTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }
    
    public WellKnown tokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
        return this;
    }
    
    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }
    
    public void setTokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }
    
    public WellKnown claimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
        return this;
    }
    
    public List<String> getClaimsSupported() {
        return claimsSupported;
    }
    
    public void setClaimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
    }
    
    public WellKnown codeChallengeMethodsSupported(List<String> codeChallengeMethodsSupported) {
        this.codeChallengeMethodsSupported = codeChallengeMethodsSupported;
        return this;
    }
    
    public List<String> getCodeChallengeMethodsSupported() {
        return codeChallengeMethodsSupported;
    }
    
    public void setCodeChallengeMethodsSupported(List<String> codeChallengeMethodsSupported) {
        this.codeChallengeMethodsSupported = codeChallengeMethodsSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WellKnown wellKnown = (WellKnown) o;
        return Objects.equals(this.issuer, wellKnown.issuer) &&
            Objects.equals(this.authorizationEndpoint, wellKnown.authorizationEndpoint) &&
            Objects.equals(this.tokenEndpoint, wellKnown.tokenEndpoint) &&
            Objects.equals(this.userinfoEndpoint, wellKnown.userinfoEndpoint) &&
            Objects.equals(this.jwksUri, wellKnown.jwksUri) &&
            Objects.equals(this.revocationEndpoint, wellKnown.revocationEndpoint) &&
            Objects.equals(this.scopesSupported, wellKnown.scopesSupported) &&
            Objects.equals(this.responseTypesSupported, wellKnown.responseTypesSupported) &&
            Objects.equals(this.grantTypesSupported, wellKnown.grantTypesSupported) &&
            Objects.equals(this.subjectTypesSupported, wellKnown.subjectTypesSupported) &&
            Objects.equals(this.idTokenSigningAlgValuesSupported, wellKnown.idTokenSigningAlgValuesSupported) &&
            Objects.equals(this.tokenEndpointAuthMethodsSupported, wellKnown.tokenEndpointAuthMethodsSupported) &&
            Objects.equals(this.claimsSupported, wellKnown.claimsSupported) &&
            Objects.equals(this.codeChallengeMethodsSupported, wellKnown.codeChallengeMethodsSupported);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, authorizationEndpoint, tokenEndpoint, userinfoEndpoint, jwksUri, revocationEndpoint, scopesSupported, responseTypesSupported, grantTypesSupported, subjectTypesSupported, idTokenSigningAlgValuesSupported, tokenEndpointAuthMethodsSupported, claimsSupported, codeChallengeMethodsSupported);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class WellKnown {\n");
        sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
        sb.append("    authorizationEndpoint: ").append(toIndentedString(authorizationEndpoint)).append("\n");
        sb.append("    tokenEndpoint: ").append(toIndentedString(tokenEndpoint)).append("\n");
        sb.append("    userinfoEndpoint: ").append(toIndentedString(userinfoEndpoint)).append("\n");
        sb.append("    jwksUri: ").append(toIndentedString(jwksUri)).append("\n");
        sb.append("    revocationEndpoint: ").append(toIndentedString(revocationEndpoint)).append("\n");
        sb.append("    scopesSupported: ").append(toIndentedString(scopesSupported)).append("\n");
        sb.append("    responseTypesSupported: ").append(toIndentedString(responseTypesSupported)).append("\n");
        sb.append("    grantTypesSupported: ").append(toIndentedString(grantTypesSupported)).append("\n");
        sb.append("    subjectTypesSupported: ").append(toIndentedString(subjectTypesSupported)).append("\n");
        sb.append("    idTokenSigningAlgValuesSupported: ").append(toIndentedString(idTokenSigningAlgValuesSupported)).append("\n");
        sb.append("    tokenEndpointAuthMethodsSupported: ").append(toIndentedString(tokenEndpointAuthMethodsSupported)).append("\n");
        sb.append("    claimsSupported: ").append(toIndentedString(claimsSupported)).append("\n");
        sb.append("    codeChallengeMethodsSupported: ").append(toIndentedString(codeChallengeMethodsSupported)).append("\n");
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