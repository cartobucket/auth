// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.util.UUID

@JsonbNillable(false)
data class ClientRequest(
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("redirect_uris")
    val redirectUris: List<String>? = null,
    @field:JsonbProperty("scopes")
    val scopes: List<UUID>? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
