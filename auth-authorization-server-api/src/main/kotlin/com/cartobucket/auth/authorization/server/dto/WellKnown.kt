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
data class WellKnown(
    @field:JsonbProperty("issuer")
    var issuer: String? = null,

    @field:JsonbProperty("authorization_endpoint")
    var authorizationEndpoint: String? = null,

    @field:JsonbProperty("token_endpoint")
    var tokenEndpoint: String? = null,

    @field:JsonbProperty("userinfo_endpoint")
    var userinfoEndpoint: String? = null,

    @field:JsonbProperty("jwks_uri")
    var jwksUri: String? = null,

    @field:JsonbProperty("revocation_endpoint")
    var revocationEndpoint: String? = null,

    @field:JsonbProperty("scopes_supported")
    var scopesSupported: List<String>? = null,

    @field:JsonbProperty("response_types_supported")
    var responseTypesSupported: List<String>? = null,

    @field:JsonbProperty("grant_types_supported")
    var grantTypesSupported: List<String>? = null,

    @field:JsonbProperty("subject_types_supported")
    var subjectTypesSupported: List<String>? = null,

    @field:JsonbProperty("id_token_signing_alg_values_supported")
    var idTokenSigningAlgValuesSupported: List<String>? = null,

    @field:JsonbProperty("token_endpoint_auth_methods_supported")
    var tokenEndpointAuthMethodsSupported: List<String>? = null,

    @field:JsonbProperty("claims_supported")
    var claimsSupported: List<String>? = null,

    @field:JsonbProperty("code_challenge_methods_supported")
    var codeChallengeMethodsSupported: List<String>? = null
) {
    enum class ResponseTypesSupportedEnum(private val _value: String) {
        CODE("code"),
        TOKEN("token"),
        CODE_ID_TOKEN("code id_token");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): ResponseTypesSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): ResponseTypesSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class SubjectTypesSupportedEnum(private val _value: String) {
        PUBLIC("public");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): SubjectTypesSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): SubjectTypesSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class IdTokenSigningAlgValuesSupportedEnum(private val _value: String) {
        RS256("RS256"),
        RS512("RS512"),
        EDDSA("EdDSA");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): IdTokenSigningAlgValuesSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): IdTokenSigningAlgValuesSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class ScopesSupportedEnum(private val _value: String) {
        OPENID("openid"),
        EMAIL("email"),
        PROFILE("profile");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): ScopesSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): ScopesSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class TokenEndpointAuthMethodsSupportedEnum(private val _value: String) {
        POST("client_secret_post"),
        BASIC("client_secret_basic");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): TokenEndpointAuthMethodsSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): TokenEndpointAuthMethodsSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class ClaimsSupportedEnum(private val _value: String) {
        AUD("aud"),
        EXP("exp"),
        FAMILY_NAME("family_name"),
        GIVEN_NAME("given_name"),
        IAT("iat"),
        ISS("iss"),
        NAME("name"),
        SUB("sub");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): ClaimsSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): ClaimsSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }

    enum class CodeChallengeMethodsSupportedEnum(private val _value: String) {
        S256("S256");

        fun value(): String = _value

        override fun toString(): String = _value

        companion object {
            @JvmStatic
            fun fromString(s: String): CodeChallengeMethodsSupportedEnum =
                entries.find { it._value == s }
                    ?: throw IllegalArgumentException("Unexpected string value '$s'")

            @JvmStatic
            fun fromValue(value: String): CodeChallengeMethodsSupportedEnum =
                entries.find { it._value == value }
                    ?: throw IllegalArgumentException("Unexpected value '$value'")
        }
    }
}
