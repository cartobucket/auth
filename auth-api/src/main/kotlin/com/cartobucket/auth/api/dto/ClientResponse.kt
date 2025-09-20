// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class ClientResponse(
    @field:JsonbProperty("id")
    val id: String? = null,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("redirect_uris")
    val redirectUris: List<String>? = null,
    @field:JsonbProperty("created_on")
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updated_on")
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("scopes")
    val scopes: List<ScopeResponse>? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
