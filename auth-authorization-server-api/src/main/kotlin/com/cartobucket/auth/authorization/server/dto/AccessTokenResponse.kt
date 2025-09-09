/* (C)2024 */
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbProperty
import jakarta.json.bind.annotation.JsonbPropertyOrder
import jakarta.json.bind.annotation.JsonbTypeAdapter
import jakarta.json.bind.adapter.JsonbAdapter
import jakarta.validation.Valid

@JsonbPropertyOrder("access_token", "token_type", "expires_in", "refresh_token", "scope", "id_token")
data class AccessTokenResponse(
    @field:JsonbProperty("access_token")
    @field:Valid
    val accessToken: String? = null,
    
    @field:JsonbProperty("token_type")
    @field:Valid
    @field:JsonbTypeAdapter(TokenTypeAdapter::class)
    val tokenType: TokenTypeEnum? = null,
    
    @field:JsonbProperty("expires_in")
    @field:Valid
    val expiresIn: Int? = null,
    
    @field:JsonbProperty("refresh_token")
    @field:Valid
    val refreshToken: String? = null,
    
    @field:JsonbProperty("scope")
    @field:Valid
    val scope: String? = null,
    
    @field:JsonbProperty("id_token")
    @field:Valid
    val idToken: String? = null
) {
    enum class TokenTypeEnum(val value: String) {
        BEARER("Bearer");
        
        override fun toString(): String = value
        
        companion object {
            @JvmStatic
            fun fromString(s: String): TokenTypeEnum =
                values().firstOrNull { it.value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")
            
            @JvmStatic
            fun fromValue(value: String): TokenTypeEnum =
                values().firstOrNull { it.value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
    
    class TokenTypeAdapter : JsonbAdapter<TokenTypeEnum, String> {
        override fun adaptToJson(value: TokenTypeEnum?): String? = value?.value
        
        override fun adaptFromJson(value: String?): TokenTypeEnum? =
            value?.let { TokenTypeEnum.fromValue(it) }
    }
}