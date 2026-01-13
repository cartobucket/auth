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
import com.cartobucket.auth.postgres.client.entities.SigningKey as PostgresSigningKey

object SigningKeyMapper {
    @JvmStatic
    fun from(signingKey: PostgresSigningKey): SigningKey {
        return SigningKey(
            id = signingKey.id,
            keyType = signingKey.keyType,
            privateKey = signingKey.privateKey,
            publicKey = signingKey.publicKey,
            metadata = signingKey.metadata,
            authorizationServerId = signingKey.authorizationServerId,
            createdOn = signingKey.createdOn,
            updatedOn = signingKey.updatedOn
        )
    }

    @JvmStatic
    fun to(signingKey: SigningKey): PostgresSigningKey {
        return PostgresSigningKey().apply {
            id = signingKey.id
            keyType = signingKey.keyType
            privateKey = signingKey.privateKey
            publicKey = signingKey.publicKey
            metadata = signingKey.metadata
            authorizationServerId = signingKey.authorizationServerId
            createdOn = signingKey.createdOn
            updatedOn = signingKey.updatedOn
        }
    }
}
