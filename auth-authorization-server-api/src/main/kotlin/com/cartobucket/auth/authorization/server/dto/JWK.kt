/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty

@JsonbNillable(false)
data class JWK(
    @field:JsonbProperty("kty")
    var kty: String? = null,

    @field:JsonbProperty("use")
    var use: String? = null,

    @field:JsonbProperty("key_ops")
    var keyOps: List<String>? = null,

    @field:JsonbProperty("alg")
    var alg: String? = null,

    @field:JsonbProperty("kid")
    var kid: String? = null,

    @field:JsonbProperty("x5c")
    var x5c: List<String>? = null,

    @field:JsonbProperty("x5t")
    var x5t: String? = null,

    @field:JsonbProperty("x5t#S256")
    var x5tS256: String? = null,

    @field:JsonbProperty("x5u")
    var x5u: String? = null,

    @field:JsonbProperty("n")
    var n: String? = null,

    @field:JsonbProperty("e")
    var e: String? = null,

    @field:JsonbProperty("d")
    var d: String? = null,

    @field:JsonbProperty("p")
    var p: String? = null,

    @field:JsonbProperty("q")
    var q: String? = null,

    @field:JsonbProperty("dp")
    var dp: String? = null,

    @field:JsonbProperty("dq")
    var dq: String? = null,

    @field:JsonbProperty("qi")
    var qi: String? = null
)
