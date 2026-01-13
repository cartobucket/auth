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

package com.revethq.auth.web.authorization.routes.mappers

import com.revethq.auth.core.authorization.dto.JWK
import com.revethq.auth.core.authorization.dto.JWKS
import com.revethq.auth.core.domain.JWK as DomainJWK

object JwksMapper {
    @JvmStatic
    fun to(jwk: DomainJWK): JWK {
        return JWK(
            alg = jwk.alg,
            e = jwk.e,
            kid = jwk.kid,
            kty = jwk.kty,
            n = jwk.n,
            use = jwk.use
        )
    }

    @JvmStatic
    fun toJwksResponse(jwksForAuthorizationServer: List<DomainJWK>): JWKS {
        return JWKS(
            keys = jwksForAuthorizationServer.map { to(it) }
        )
    }
}
