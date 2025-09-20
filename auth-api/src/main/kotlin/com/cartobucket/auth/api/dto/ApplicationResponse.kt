// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class ApplicationResponse(
    @field:JsonbProperty("id")
    val id: String? = null,
    @field:JsonbProperty("authorization_server_id")
    @field:NotNull
    val authorizationServerId: UUID,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("client_id")
    val clientId: String? = null,
    @field:JsonbProperty("client_secret")
    val clientSecret: String? = null,
    @field:JsonbProperty("profile")
    val profile: Any? = null,
    @field:JsonbProperty("created_on")
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updated_on")
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
    @field:JsonbProperty("scopes")
    @field:NotNull
    val scopes: List<ScopeResponse>,
)
