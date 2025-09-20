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

import com.cartobucket.auth.data.domain.SigningKey

object SigningKeyMapper {
    fun from(signingKey: com.cartobucket.auth.postgres.client.entities.SigningKey): SigningKey {
        val domainSigningKey = SigningKey()
        domainSigningKey.id = signingKey.id
        domainSigningKey.keyType = signingKey.keyType
        domainSigningKey.privateKey = signingKey.privateKey
        domainSigningKey.publicKey = signingKey.publicKey
        // Note: jwk property exists only in entity, not in domain
        domainSigningKey.authorizationServerId = signingKey.authorizationServerId
        domainSigningKey.createdOn = signingKey.createdOn
        return domainSigningKey
    }

    fun to(signingKey: SigningKey): com.cartobucket.auth.postgres.client.entities.SigningKey {
        val entitySigningKey =
            com.cartobucket.auth.postgres.client.entities
                .SigningKey()
        entitySigningKey.id = signingKey.id
        entitySigningKey.keyType = signingKey.keyType
        entitySigningKey.privateKey = signingKey.privateKey
        entitySigningKey.publicKey = signingKey.publicKey
        // Note: jwk property exists only in entity, not in domain
        entitySigningKey.authorizationServerId = signingKey.authorizationServerId
        entitySigningKey.createdOn = signingKey.createdOn
        return entitySigningKey
    }
}
