// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.util.UUID

@JsonbNillable(false)
data class ApplicationRequest(
    @field:JsonbProperty("authorization_server_id")
    @field:NotNull
    val authorizationServerId: UUID,
    @field:JsonbProperty("name")
    @field:NotNull
    val name: String,
    @field:JsonbProperty("client_id")
    val clientId: String? = null,
    @field:JsonbProperty("profile")
    val profile: Any? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
    @field:JsonbProperty("scopes")
    val scopes: List<UUID>? = null,
)
