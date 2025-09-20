// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.util.UUID

@JsonbNillable(false)
data class ScopeRequest(
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
