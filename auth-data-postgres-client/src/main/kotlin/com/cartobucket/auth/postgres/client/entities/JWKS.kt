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

package com.cartobucket.auth.postgres.client.entities

import jakarta.json.bind.annotation.JsonbProperty

class JWKS {
    @get:JsonbProperty("keys")
    @set:JsonbProperty("keys")
    var keys: MutableList<JWK>? = null

    fun keys(keys: MutableList<JWK>?): JWKS {
        this.keys = keys
        return this
    }

    fun addKeysItem(keysItem: JWK): JWKS {
        if (this.keys == null) {
            this.keys = mutableListOf()
        }
        this.keys!!.add(keysItem)
        return this
    }

    fun removeKeysItem(keysItem: JWK): JWKS {
        this.keys?.remove(keysItem)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val jwks = other as JWKS
        return keys == jwks.keys
    }

    override fun hashCode(): Int {
        return keys?.hashCode() ?: 0
    }

    override fun toString(): String {
        return """
            class JWKS {
                keys: ${toIndentedString(keys)}
            }
        """.trimIndent()
    }

    private fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }
}
