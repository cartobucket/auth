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

import com.cartobucket.auth.data.domain.Client
import com.cartobucket.auth.postgres.client.entities.Client as PostgresClient

object ClientMapper {
    @JvmStatic
    fun from(client: PostgresClient): Client {
        return Client(
            name = client.name,
            scopes = client.scopes?.map { ScopeMapper.fromNoAuthorizationServer(it) } ?: emptyList(),
            id = client.id,
            redirectUris = client.redirectUris,
            authorizationServerId = client.authorizationServerId,
            metadata = client.metadata,
            updatedOn = client.updatedOn,
            createdOn = client.createdOn
        )
    }

    @JvmStatic
    fun to(client: Client): PostgresClient {
        return PostgresClient().apply {
            name = client.name
            id = client.id
            // TODO: This should come from the client, this is just an oversight, please fix.
            clientId = client.id?.toString()
            redirectUris = client.redirectUris
            authorizationServerId = client.authorizationServerId
            metadata = client.metadata
            updatedOn = client.updatedOn
            createdOn = client.createdOn
        }
    }
}
