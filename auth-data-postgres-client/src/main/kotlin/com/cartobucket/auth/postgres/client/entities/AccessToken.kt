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
import jakarta.json.bind.annotation.JsonbTypeAdapter
import jakarta.json.bind.adapter.JsonbAdapter

class AccessToken {
    @get:JsonbProperty("access_token")
    @set:JsonbProperty("access_token")
    var accessToken: String? = null

    @JsonbTypeAdapter(TokenTypeAdapter::class)
    enum class TokenTypeEnum(val value: String) {
        BEARER("Bearer");

        override fun toString(): String = value

        companion object {
            fun fromString(s: String): TokenTypeEnum {
                for (b in entries) {
                    if (b.value == s) {
                        return b
                    }
                }
                throw IllegalArgumentException("Unexpected string value '$s'")
            }

            fun fromValue(value: String): TokenTypeEnum {
                for (b in entries) {
                    if (b.value == value) {
                        return b
                    }
                }
                throw IllegalArgumentException("Unexpected value '$value'")
            }
        }
    }

    class TokenTypeAdapter : JsonbAdapter<TokenTypeEnum?, String?> {
        override fun adaptToJson(value: TokenTypeEnum?): String? {
            return value?.value
        }

        override fun adaptFromJson(value: String?): TokenTypeEnum? {
            return value?.let { TokenTypeEnum.fromValue(it) }
        }
    }

    @get:JsonbProperty("token_type")
    @set:JsonbProperty("token_type")
    var tokenType: TokenTypeEnum? = null

    @get:JsonbProperty("refresh_token")
    @set:JsonbProperty("refresh_token")
    var refreshToken: String? = null

    @get:JsonbProperty("expires_in")
    @set:JsonbProperty("expires_in")
    var expiresIn: Int? = null

    @get:JsonbProperty("id_token")
    @set:JsonbProperty("id_token")
    var idToken: String? = null

    @get:JsonbProperty("scope")
    @set:JsonbProperty("scope")
    var scope: String? = null

    fun accessToken(accessToken: String?): AccessToken {
        this.accessToken = accessToken
        return this
    }

    fun tokenType(tokenType: TokenTypeEnum?): AccessToken {
        this.tokenType = tokenType
        return this
    }

    fun refreshToken(refreshToken: String?): AccessToken {
        this.refreshToken = refreshToken
        return this
    }

    fun expiresIn(expiresIn: Int?): AccessToken {
        this.expiresIn = expiresIn
        return this
    }

    fun idToken(idToken: String?): AccessToken {
        this.idToken = idToken
        return this
    }

    fun scope(scope: String?): AccessToken {
        this.scope = scope
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AccessToken
        return accessToken == that.accessToken &&
                tokenType == that.tokenType &&
                refreshToken == that.refreshToken &&
                expiresIn == that.expiresIn &&
                idToken == that.idToken &&
                scope == that.scope
    }

    override fun hashCode(): Int {
        return listOf(accessToken, tokenType, refreshToken, expiresIn, idToken, scope).hashCode()
    }

    override fun toString(): String {
        return """
            class AccessTokenResponse {
                accessToken: ${toIndentedString(accessToken)}
                tokenType: ${toIndentedString(tokenType)}
                refreshToken: ${toIndentedString(refreshToken)}
                expiresIn: ${toIndentedString(expiresIn)}
                idToken: ${toIndentedString(idToken)}
                scope: ${toIndentedString(scope)}
            }
        """.trimIndent()
    }

    private fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }
}
