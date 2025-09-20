// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty

@JsonbNillable(false)
data class AuthorizationServerRequest(
    @field:JsonbProperty("server_url")
    val serverUrl: String? = null,
    @field:JsonbProperty("audience")
    val audience: String? = null,
    @field:JsonbProperty("client_credentials_token_expiration")
    val clientCredentialsTokenExpiration: Int? = null,
    @field:JsonbProperty("authorization_code_token_expiration")
    val authorizationCodeTokenExpiration: Int? = null,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
