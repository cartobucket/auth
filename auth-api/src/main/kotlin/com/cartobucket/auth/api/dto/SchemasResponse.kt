// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class SchemasResponse(
    @field:JsonbProperty("schemas")
    val schemas: List<SchemaResponse>? = null,
    @field:JsonbProperty("page")
    @field:NotNull
    val page: Page,
)
