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

package com.cartobucket.auth.postgres.client.entities.mappers

import com.cartobucket.auth.data.domain.JWK

object JWKMapper {
    fun from(jwk: com.cartobucket.auth.postgres.client.entities.JWK): JWK {
        val domainJwk = JWK()
        domainJwk.alg = jwk.alg
        domainJwk.kid = jwk.kid
        domainJwk.e = jwk.e
        domainJwk.kty = jwk.kty
        domainJwk.n = jwk.n
        domainJwk.use = jwk.use
        domainJwk.x5c = jwk.x5c?.toMutableList()
        domainJwk.x5t = jwk.x5t
        domainJwk.x5tHashS256 = jwk.x5tHashS256
        return domainJwk
    }

    fun to(jwk: JWK): com.cartobucket.auth.postgres.client.entities.JWK {
        val entityJwk =
            com.cartobucket.auth.postgres.client.entities
                .JWK()
        entityJwk.alg = jwk.alg
        entityJwk.kid = jwk.kid
        entityJwk.e = jwk.e
        entityJwk.kty = jwk.kty
        entityJwk.n = jwk.n
        entityJwk.use = jwk.use
        entityJwk.x5c = jwk.x5c?.toList()
        entityJwk.x5t = jwk.x5t
        entityJwk.x5tHashS256 = jwk.x5tHashS256
        return entityJwk
    }
}
