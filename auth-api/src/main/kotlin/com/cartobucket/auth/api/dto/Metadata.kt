// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid

/**
 *
 */
@JsonbNillable(false)
data class Metadata(
    @field:JsonbProperty("properties")
    @field:Valid
    val properties: Map<String, Any>? = null,
    @field:JsonbProperty("identifiers")
    @field:Valid
    val identifiers: List<MetadataIdentifiersInner>? = null,
    @field:JsonbProperty("schemaValidations")
    @field:Valid
    val schemaValidations: List<MetadataSchemaValidationsInner>? = null,
)
