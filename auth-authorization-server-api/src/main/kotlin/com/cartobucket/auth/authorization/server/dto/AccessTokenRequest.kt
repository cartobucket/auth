/* (C)2024 */
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.json.bind.annotation.JsonbTypeAdapter
import jakarta.json.bind.adapter.JsonbAdapter
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class AccessTokenRequest(
    @field:JsonbProperty("client_id")
    @field:NotNull
    @field:Valid
    val clientId: String? = null,
    
    @field:JsonbProperty("client_secret")
    @field:Valid
    val clientSecret: String? = null,
    
    @field:JsonbProperty("grant_type")
    @field:NotNull
    @field:Valid
    @field:JsonbTypeAdapter(GrantTypeAdapter::class)
    val grantType: GrantTypeEnum? = null,
    
    @field:JsonbProperty("code")
    @field:Valid
    val code: String? = null,
    
    @field:JsonbProperty("redirect_uri")
    @field:Valid
    val redirectUri: String? = null,
    
    @field:JsonbProperty("code_verifier")
    @field:Valid
    val codeVerifier: String? = null,
    
    @field:JsonbProperty("refresh_token")
    @field:Valid
    val refreshToken: String? = null,
    
    @field:JsonbProperty("scope")
    @field:Valid
    val scope: String? = null
) {
    enum class GrantTypeEnum(val value: String) {
        CLIENT_CREDENTIALS("client_credentials"),
        AUTHORIZATION_CODE("authorization_code"),
        REFRESH_TOKEN("refresh_token");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): GrantTypeEnum = 
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): GrantTypeEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    class GrantTypeAdapter : JsonbAdapter<GrantTypeEnum, String> {
        override fun adaptToJson(value: GrantTypeEnum?): String? = value?.value
        
        override fun adaptFromJson(value: String?): GrantTypeEnum? = 
            value?.let { GrantTypeEnum.fromValue(it) }
    }
}