/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.persistence.entities

import jakarta.json.bind.annotation.JsonbProperty

class JWK {
    @get:JsonbProperty("kid")
    @set:JsonbProperty("kid")
    var kid: String? = null

    @get:JsonbProperty("kty")
    @set:JsonbProperty("kty")
    var kty: String? = null

    @get:JsonbProperty("alg")
    @set:JsonbProperty("alg")
    var alg: String? = null

    @get:JsonbProperty("use")
    @set:JsonbProperty("use")
    var use: String? = null

    @get:JsonbProperty("n")
    @set:JsonbProperty("n")
    var n: String? = null

    @get:JsonbProperty("e")
    @set:JsonbProperty("e")
    var e: String? = null

    @get:JsonbProperty("x5c")
    @set:JsonbProperty("x5c")
    var x5c: MutableList<String>? = null

    @get:JsonbProperty("x5t")
    @set:JsonbProperty("x5t")
    var x5t: String? = null

    @get:JsonbProperty("x5t#S256")
    @set:JsonbProperty("x5t#S256")
    var x5tHashS256: String? = null

    fun kid(kid: String?): JWK {
        this.kid = kid
        return this
    }

    fun kty(kty: String?): JWK {
        this.kty = kty
        return this
    }

    fun alg(alg: String?): JWK {
        this.alg = alg
        return this
    }

    fun use(use: String?): JWK {
        this.use = use
        return this
    }

    fun n(n: String?): JWK {
        this.n = n
        return this
    }

    fun e(e: String?): JWK {
        this.e = e
        return this
    }

    fun x5c(x5c: MutableList<String>?): JWK {
        this.x5c = x5c
        return this
    }

    fun addX5cItem(x5cItem: String): JWK {
        if (this.x5c == null) {
            this.x5c = mutableListOf()
        }
        this.x5c!!.add(x5cItem)
        return this
    }

    fun removeX5cItem(x5cItem: String): JWK {
        this.x5c?.remove(x5cItem)
        return this
    }

    fun x5t(x5t: String?): JWK {
        this.x5t = x5t
        return this
    }

    fun x5tHashS256(x5tHashS256: String?): JWK {
        this.x5tHashS256 = x5tHashS256
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val jwk = other as JWK
        return kid == jwk.kid &&
                kty == jwk.kty &&
                alg == jwk.alg &&
                use == jwk.use &&
                n == jwk.n &&
                e == jwk.e &&
                x5c == jwk.x5c &&
                x5t == jwk.x5t &&
                x5tHashS256 == jwk.x5tHashS256
    }

    override fun hashCode(): Int {
        return listOf(kid, kty, alg, use, n, e, x5c, x5t, x5tHashS256).hashCode()
    }

    override fun toString(): String {
        return """
            class JWK {
                kid: ${toIndentedString(kid)}
                kty: ${toIndentedString(kty)}
                alg: ${toIndentedString(alg)}
                use: ${toIndentedString(use)}
                n: ${toIndentedString(n)}
                e: ${toIndentedString(e)}
                x5c: ${toIndentedString(x5c)}
                x5t: ${toIndentedString(x5t)}
                x5tHashS256: ${toIndentedString(x5tHashS256)}
            }
        """.trimIndent()
    }

    private fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }
}
