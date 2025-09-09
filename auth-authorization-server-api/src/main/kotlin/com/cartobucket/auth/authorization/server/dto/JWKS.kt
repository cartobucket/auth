// (C)2024
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty

@JsonbNillable(false)
data class JWKS(
    @field:JsonbProperty("keys")
    val keys: List<JWK>? = null,
)
