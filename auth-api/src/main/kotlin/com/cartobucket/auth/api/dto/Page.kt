// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.Valid

/**
 *
 */
@JsonbNillable(false)
data class Page(
    @field:JsonbProperty("next")
    @field:Valid
    val next: String? = null,
    @field:JsonbProperty("previous")
    @field:Valid
    val previous: String? = null,
)
