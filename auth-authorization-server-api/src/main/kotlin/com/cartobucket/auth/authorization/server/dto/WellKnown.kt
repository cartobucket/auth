/* (C)2024 */
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid

@JsonbNillable(false)
data class WellKnown(
    @field:JsonbProperty("issuer")
    @field:Valid
    val issuer: String? = null,
    
    @field:JsonbProperty("authorization_endpoint")
    @field:Valid
    val authorizationEndpoint: String? = null,
    
    @field:JsonbProperty("token_endpoint")
    @field:Valid
    val tokenEndpoint: String? = null,
    
    @field:JsonbProperty("userinfo_endpoint")
    @field:Valid
    val userinfoEndpoint: String? = null,
    
    @field:JsonbProperty("jwks_uri")
    @field:Valid
    val jwksUri: String? = null,
    
    @field:JsonbProperty("revocation_endpoint")
    @field:Valid
    val revocationEndpoint: String? = null,
    
    @field:JsonbProperty("scopes_supported")
    @field:Valid
    val scopesSupported: List<String>? = null,
    
    @field:JsonbProperty("response_types_supported")
    @field:Valid
    val responseTypesSupported: List<String>? = null,
    
    @field:JsonbProperty("grant_types_supported")
    @field:Valid
    val grantTypesSupported: List<String>? = null,
    
    @field:JsonbProperty("subject_types_supported")
    @field:Valid
    val subjectTypesSupported: List<String>? = null,
    
    @field:JsonbProperty("id_token_signing_alg_values_supported")
    @field:Valid
    val idTokenSigningAlgValuesSupported: List<String>? = null,
    
    @field:JsonbProperty("token_endpoint_auth_methods_supported")
    @field:Valid
    val tokenEndpointAuthMethodsSupported: List<String>? = null,
    
    @field:JsonbProperty("claims_supported")
    @field:Valid
    val claimsSupported: List<String>? = null,
    
    @field:JsonbProperty("code_challenge_methods_supported")
    @field:Valid
    val codeChallengeMethodsSupported: List<String>? = null
) {
    enum class ResponseTypesSupportedEnum(val value: String) {
        CODE("code"),
        TOKEN("token"),
        CODE_ID_TOKEN("code id_token");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): ResponseTypesSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): ResponseTypesSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class SubjectTypesSupportedEnum(val value: String) {
        PUBLIC("public");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): SubjectTypesSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): SubjectTypesSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class IdTokenSigningAlgValuesSupportedEnum(val value: String) {
        RS256("RS256"),
        RS512("RS512"),
        EDDSA("EdDSA");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): IdTokenSigningAlgValuesSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): IdTokenSigningAlgValuesSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class ScopesSupportedEnum(val value: String) {
        OPENID("openid"),
        EMAIL("email"),
        PROFILE("profile");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): ScopesSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): ScopesSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class TokenEndpointAuthMethodsSupportedEnum(val value: String) {
        POST("client_secret_post"),
        BASIC("client_secret_basic");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): TokenEndpointAuthMethodsSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): TokenEndpointAuthMethodsSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class ClaimsSupportedEnum(val value: String) {
        AUD("aud"),
        EXP("exp"),
        FAMILY_NAME("family_name"),
        GIVEN_NAME("given_name"),
        IAT("iat"),
        ISS("iss"),
        NAME("name"),
        SUB("sub");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): ClaimsSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): ClaimsSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    enum class CodeChallengeMethodsSupportedEnum(val value: String) {
        S256("S256");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): CodeChallengeMethodsSupportedEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): CodeChallengeMethodsSupportedEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
}