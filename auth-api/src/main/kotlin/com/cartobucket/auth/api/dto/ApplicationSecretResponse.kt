// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class ApplicationSecretResponse(
    @field:JsonbProperty("id")
    val id: UUID? = null,
    @field:JsonbProperty("client_secret")
    val clientSecret: String? = null,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("expires_in")
    val expiresIn: Int? = null,
    @field:JsonbProperty("application_id")
    val applicationId: UUID? = null,
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("scopes")
    @field:NotNull
    val scopes: List<ScopeResponse>,
    @field:JsonbProperty("created_on")
    @field:NotNull
    val createdOn: OffsetDateTime,
)
