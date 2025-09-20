// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull
import java.util.UUID

@JsonbNillable(false)
data class SchemaResponse(
    @field:JsonbProperty("id")
    val id: UUID? = null,
    @field:JsonbProperty("authorization_server")
    @field:NotNull
    val authorizationServer: UUID,
    @field:JsonbProperty("name")
    val name: String? = null,
    @field:JsonbProperty("schema_version")
    val schemaVersion: String? = null,
    @field:JsonbProperty("schema")
    val schema: Any? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
