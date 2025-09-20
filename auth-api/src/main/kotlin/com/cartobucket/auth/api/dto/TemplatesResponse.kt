// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class TemplatesResponse(
    @field:JsonbProperty("templates")
    val templates: List<TemplateResponse>? = null,
    @field:JsonbProperty("page")
    @field:NotNull
    val page: Page,
)
