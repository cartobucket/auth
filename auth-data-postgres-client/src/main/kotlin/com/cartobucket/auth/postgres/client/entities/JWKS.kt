package com.cartobucket.auth.postgres.client.entities

import jakarta.json.bind.annotation.JsonbProperty
import java.util.Objects

/**
 *
 */
@jakarta.annotation.Generated(
    value = ["org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen"],
    date = "2023-05-23T21:07:45.072820760-07:00[America/Los_Angeles]",
)
class JWKS {
    @JsonbProperty("keys")
    var keys: List<JWK>? = null

    fun keys(keys: List<JWK>?): JWKS {
        this.keys = keys
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val jwks = other as JWKS
        return keys == jwks.keys
    }

    override fun hashCode(): Int = Objects.hash(keys)

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class JWKS {\n")
        sb.append("    keys: ").append(toIndentedString(keys)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    private fun toIndentedString(o: Any?): String = o?.toString()?.replace("\n", "\n    ") ?: "null"
}
