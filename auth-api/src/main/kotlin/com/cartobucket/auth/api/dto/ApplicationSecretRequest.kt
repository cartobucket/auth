// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.util.UUID

@JsonbNillable(false)
data class ApplicationSecretRequest(
    @field:JsonbProperty("application_id")
    @field:NotNull
    val applicationId: UUID,
    @field:JsonbProperty("name")
    @field:NotNull
    val name: String,
    @field:JsonbProperty("scopes")
    @field:NotNull
    val scopes: List<UUID>,
    @field:JsonbProperty("expires_in")
    val expiresIn: Int? = null,
)
