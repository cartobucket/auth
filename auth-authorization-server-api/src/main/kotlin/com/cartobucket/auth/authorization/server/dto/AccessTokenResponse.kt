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

import jakarta.json.bind.adapter.JsonbAdapter
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.json.bind.annotation.JsonbPropertyOrder
import jakarta.json.bind.annotation.JsonbTypeAdapter

@JsonbPropertyOrder("access_token", "token_type", "expires_in", "refresh_token", "scope", "id_token")
data class AccessTokenResponse(
    @field:JsonbProperty("access_token")
    var accessToken: String? = null,

    @field:JsonbProperty("token_type")
    @field:JsonbTypeAdapter(TokenTypeAdapter::class)
    var tokenType: TokenTypeEnum? = null,

    @field:JsonbProperty("expires_in")
    var expiresIn: Int? = null,

    @field:JsonbProperty("refresh_token")
    var refreshToken: String? = null,

    @field:JsonbProperty("scope")
    var scope: String? = null,

    @field:JsonbProperty("id_token")
    var idToken: String? = null
) {
    enum class TokenTypeEnum(private val _value: String) {
        BEARER("Bearer");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): TokenTypeEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): TokenTypeEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    class TokenTypeAdapter : JsonbAdapter<TokenTypeEnum, String> {
        override fun adaptToJson(value: TokenTypeEnum?): String? = value?.value()

        override fun adaptFromJson(value: String?): TokenTypeEnum? =
            value?.let { TokenTypeEnum.fromValue(it) }
    }
}
