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

package com.cartobucket.auth.authorization.server.routes.mappers;


import com.cartobucket.auth.model.generated.JWK;
import com.cartobucket.auth.model.generated.JWKS;

import java.util.List;

public class JwksMapper {
    public static JWK to(com.cartobucket.auth.data.domain.JWK jwk) {
        var jwkModel = new JWK();
        jwkModel.setAlg(jwk.getAlg());
        jwkModel.setE(jwk.getE());
        jwkModel.setKid(jwk.getKid());
        jwkModel.setKty(jwk.getKty());
        jwkModel.setN(jwk.getN());
        jwkModel.setUse(jwk.getUse());
        return jwkModel;
    }

    public static JWKS toJwksResponse(List<com.cartobucket.auth.data.domain.JWK> jwksForAuthorizationServer) {
        var jwks = new JWKS();
        jwks.setKeys(jwksForAuthorizationServer.stream().map(JwksMapper::to).toList());
        return jwks;
    }
}
