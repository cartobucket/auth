// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class ApplicationsResponse(
    @field:JsonbProperty("applications")
    val applications: List<ApplicationResponse>? = null,
    @field:JsonbProperty("page")
    @field:NotNull
    val page: Page,
)
