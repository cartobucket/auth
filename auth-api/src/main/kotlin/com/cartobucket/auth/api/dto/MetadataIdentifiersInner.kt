// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid

@JsonbNillable(false)
data class MetadataIdentifiersInner(
    @field:JsonbProperty("system")
    @field:Valid
    val system: String? = null,
    @field:JsonbProperty("value")
    @field:Valid
    val value: String? = null,
)
