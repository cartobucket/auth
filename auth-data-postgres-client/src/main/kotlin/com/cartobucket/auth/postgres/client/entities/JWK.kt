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
class JWK {
    @JsonbProperty("kid")
    var kid: String? = null

    @JsonbProperty("kty")
    var kty: String? = null

    @JsonbProperty("alg")
    var alg: String? = null

    @JsonbProperty("use")
    var use: String? = null

    @JsonbProperty("n")
    var n: String? = null

    @JsonbProperty("e")
    var e: String? = null

    @JsonbProperty("x5c")
    var x5c: List<String>? = null

    @JsonbProperty("x5t")
    var x5t: String? = null

    @JsonbProperty("x5t#S256")
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

    fun x5c(x5c: List<String>?): JWK {
        this.x5c = x5c
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

    override fun hashCode(): Int = Objects.hash(kid, kty, alg, use, n, e, x5c, x5t, x5tHashS256)

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class JWK {\n")
        sb.append("    kid: ").append(toIndentedString(kid)).append("\n")
        sb.append("    kty: ").append(toIndentedString(kty)).append("\n")
        sb.append("    alg: ").append(toIndentedString(alg)).append("\n")
        sb.append("    use: ").append(toIndentedString(use)).append("\n")
        sb.append("    n: ").append(toIndentedString(n)).append("\n")
        sb.append("    e: ").append(toIndentedString(e)).append("\n")
        sb.append("    x5c: ").append(toIndentedString(x5c)).append("\n")
        sb.append("    x5t: ").append(toIndentedString(x5t)).append("\n")
        sb.append("    x5tHashS256: ").append(toIndentedString(x5tHashS256)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    private fun toIndentedString(o: Any?): String = o?.toString()?.replace("\n", "\n    ") ?: "null"
}
