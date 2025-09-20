// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class UserResponse(
    @field:JsonbProperty("id")
    val id: String? = null,
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("username")
    val username: String? = null,
    @field:JsonbProperty("email")
    val email: String? = null,
    @field:JsonbProperty("profile")
    val profile: Any? = null,
    @field:JsonbProperty("created_on")
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updated_on")
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
