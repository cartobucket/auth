// (C)2024
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty

@JsonbNillable(false)
data class JWK(
    @field:JsonbProperty("kty")
    val kty: String? = null,
    @field:JsonbProperty("use")
    val use: String? = null,
    @field:JsonbProperty("key_ops")
    val keyOps: List<String>? = null,
    @field:JsonbProperty("alg")
    val alg: String? = null,
    @field:JsonbProperty("kid")
    val kid: String? = null,
    @field:JsonbProperty("x5c")
    val x5c: List<String>? = null,
    @field:JsonbProperty("x5t")
    val x5t: String? = null,
    @field:JsonbProperty("x5t#S256")
    val x5tS256: String? = null,
    @field:JsonbProperty("x5u")
    val x5u: String? = null,
    @field:JsonbProperty("n")
    val n: String? = null,
    @field:JsonbProperty("e")
    val e: String? = null,
    @field:JsonbProperty("d")
    val d: String? = null,
    @field:JsonbProperty("p")
    val p: String? = null,
    @field:JsonbProperty("q")
    val q: String? = null,
    @field:JsonbProperty("dp")
    val dp: String? = null,
    @field:JsonbProperty("dq")
    val dq: String? = null,
    @field:JsonbProperty("qi")
    val qi: String? = null,
)
