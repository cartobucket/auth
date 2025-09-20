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

import jakarta.json.bind.adapter.JsonbAdapter
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.json.bind.annotation.JsonbTypeAdapter
import java.util.Objects

/**
 *
 */
@jakarta.annotation.Generated(
    value = ["org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen"],
    date = "2023-05-29T17:50:42.716026387-07:00[America/Los_Angeles]",
)
class AccessToken {
    @JsonbProperty("access_token")
    var accessToken: String? = null

    enum class TokenTypeEnum(
        val value: String,
    ) {
        BEARER("Bearer"),
        ;

        override fun toString(): String = value

        companion object {
            fun fromString(s: String): TokenTypeEnum {
                for (b in TokenTypeEnum.entries) {
                    if (b.value == s) {
                        return b
                    }
                }
                throw IllegalArgumentException("Unexpected string value '$s'")
            }

            fun fromValue(value: String): TokenTypeEnum {
                for (b in TokenTypeEnum.entries) {
                    if (b.value == value) {
                        return b
                    }
                }
                throw IllegalArgumentException("Unexpected value '$value'")
            }
        }
    }

    class TokenTypeAdapter : JsonbAdapter<TokenTypeEnum, String> {
        override fun adaptToJson(value: TokenTypeEnum?): String? = value?.value

        override fun adaptFromJson(value: String?): TokenTypeEnum? = value?.let { TokenTypeEnum.fromValue(it) }
    }

    @JsonbTypeAdapter(TokenTypeAdapter::class)
    @JsonbProperty("token_type")
    var tokenType: TokenTypeEnum? = null

    @JsonbProperty("refresh_token")
    var refreshToken: String? = null

    @JsonbProperty("expires_in")
    var expiresIn: Int? = null

    @JsonbProperty("id_token")
    var idToken: String? = null

    @JsonbProperty("scope")
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

    override fun hashCode(): Int = Objects.hash(accessToken, tokenType, refreshToken, expiresIn, idToken, scope)

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class AccessToken {\n")
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n")
        sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n")
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n")
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n")
        sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n")
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private fun toIndentedString(o: Any?): String = o?.toString()?.replace("\n", "\n    ") ?: "null"
}
