// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class AuthorizationServersResponse(
    @field:JsonbProperty("authorization_servers")
    val authorizationServers: List<AuthorizationServerResponse>? = null,
    @field:JsonbProperty("page")
    @field:NotNull
    val page: Page,
)
