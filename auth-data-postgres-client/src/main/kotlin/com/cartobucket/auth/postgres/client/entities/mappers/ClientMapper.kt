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

object ClientMapper {
    fun from(client: com.cartobucket.auth.postgres.client.entities.Client): Client {
        val domainClient = Client()
        domainClient.name = client.name
        domainClient.scopes = client.scopes?.map { ScopeMapper.fromNoAuthorizationServer(it) }
        domainClient.id = client.id
        domainClient.redirectUris = client.redirectUris
        domainClient.authorizationServerId = client.authorizationServerId
        domainClient.metadata = client.metadata
        domainClient.updatedOn = client.updatedOn
        domainClient.createdOn = client.createdOn
        // Note: clientId property exists only in entity, not in domain
        return domainClient
    }

    fun to(client: Client): com.cartobucket.auth.postgres.client.entities.Client {
        val entityClient =
            com.cartobucket.auth.postgres.client.entities
                .Client()
        entityClient.name = client.name
        entityClient.id = client.id
        entityClient.redirectUris = client.redirectUris
        entityClient.authorizationServerId = client.authorizationServerId
        entityClient.metadata = client.metadata
        entityClient.updatedOn = client.updatedOn
        entityClient.createdOn = client.createdOn
        // Note: entity clientId will be set elsewhere if needed
        return entityClient
    }
}
