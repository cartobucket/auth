// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid
import java.time.OffsetDateTime
import java.util.UUID

/**
 *
 */
@JsonbNillable(false)
data class ScopeResponse(
    @field:JsonbProperty("id")
    @field:Valid
    val id: String? = null,
    @field:JsonbProperty("authorizationServerId")
    @field:Valid
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("createdOn")
    @field:Valid
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updatedOn")
    @field:Valid
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("name")
    @field:Valid
    val name: String? = null,
    @field:JsonbProperty("metadata")
    @field:Valid
    val metadata: Metadata? = null,
)
