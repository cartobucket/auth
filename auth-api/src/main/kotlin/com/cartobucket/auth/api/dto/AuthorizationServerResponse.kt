// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime

@JsonbNillable(false)
data class AuthorizationServerResponse(
    @field:JsonbProperty("id")
    val id: String? = null,
    @field:JsonbProperty("server_url")
    val serverUrl: String? = null,
    @field:JsonbProperty("audience")
    val audience: String? = null,
    @field:JsonbProperty("client_credentials_token_expiration")
    val clientCredentialsTokenExpiration: Int? = null,
    @field:JsonbProperty("authorization_code_token_expiration")
    val authorizationCodeTokenExpiration: Int? = null,
    @field:JsonbProperty("name")
    @field:NotNull
    val name: String,
    @field:JsonbProperty("created_on")
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updated_on")
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
    @field:JsonbProperty("scopes")
    val scopes: List<ScopeResponse>? = null,
)
