// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class MetadataSchemaValidationsInner(
    @field:JsonbProperty("schemaId")
    @field:Valid
    val schemaId: UUID? = null,
    @field:JsonbProperty("isValid")
    @field:Valid
    val isValid: Boolean? = null,
    @field:JsonbProperty("validatedOn")
    @field:Valid
    val validatedOn: OffsetDateTime? = null,
)
