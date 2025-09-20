// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.util.UUID

@JsonbNillable(false)
data class UserRequestFilter(
    @field:JsonbProperty("authorization_server_ids")
    val authorizationServerIds: List<UUID>? = null,
)
