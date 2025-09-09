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

package com.cartobucket.auth.data.domain

enum class TokenTypeEnum(
    private val value: String,
) {
    BEARER("Bearer"),
    ;

    fun value(): String = value

    override fun toString(): String = value

    companion object {
        /**
         * Convert a String into String, as specified in the
         * [See JAX RS 2.0 Specification, section 3.2, p. 12](https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html)
         */
        fun fromString(s: String): TokenTypeEnum =
            values().find { it.value == s }
                ?: throw IllegalArgumentException("Unexpected string value '$s'")

        fun fromValue(value: String): TokenTypeEnum =
            values().find { it.value == value }
                ?: throw IllegalArgumentException("Unexpected value '$value'")
    }
}
