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
import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.json.bind.annotation.JsonbTypeAdapter
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class AccessTokenRequest(
    @field:JsonbProperty("client_id")
    @field:NotNull
    var clientId: String? = null,

    @field:JsonbProperty("client_secret")
    var clientSecret: String? = null,

    @field:JsonbProperty("grant_type")
    @field:NotNull
    @field:JsonbTypeAdapter(GrantTypeAdapter::class)
    var grantType: GrantTypeEnum? = null,

    @field:JsonbProperty("code")
    var code: String? = null,

    @field:JsonbProperty("redirect_uri")
    var redirectUri: String? = null,

    @field:JsonbProperty("code_verifier")
    var codeVerifier: String? = null,

    @field:JsonbProperty("refresh_token")
    var refreshToken: String? = null,

    @field:JsonbProperty("scope")
    var scope: String? = null
) {
    enum class GrantTypeEnum(private val _value: String) {
        CLIENT_CREDENTIALS("client_credentials"),
        AUTHORIZATION_CODE("authorization_code"),
        REFRESH_TOKEN("refresh_token");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): GrantTypeEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): GrantTypeEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    class GrantTypeAdapter : JsonbAdapter<GrantTypeEnum, String> {
        override fun adaptToJson(value: GrantTypeEnum?): String? = value?.value()

        override fun adaptFromJson(value: String?): GrantTypeEnum? =
            value?.let { GrantTypeEnum.fromValue(it) }
    }
}
