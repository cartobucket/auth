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

package com.revethq.auth.persistence.entities.mappers

import com.revethq.auth.core.domain.JWK
import com.revethq.auth.persistence.entities.JWK as PostgresJWK

object JWKMapper {
    @JvmStatic
    fun from(jwk: PostgresJWK): JWK {
        return JWK(
            alg = jwk.alg,
            kid = jwk.kid,
            e = jwk.e,
            kty = jwk.kty,
            n = jwk.n,
            use = jwk.use,
            x5c = jwk.x5c,
            x5t = jwk.x5t,
            x5tHashS256 = jwk.x5tHashS256
        )
    }

    @JvmStatic
    fun to(jwk: JWK): PostgresJWK {
        return PostgresJWK().apply {
            alg = jwk.alg
            kid = jwk.kid
            e = jwk.e
            kty = jwk.kty
            n = jwk.n
            use = jwk.use
            x5c = jwk.x5c
            x5t = jwk.x5t
            x5tHashS256 = jwk.x5tHashS256
        }
    }
}
