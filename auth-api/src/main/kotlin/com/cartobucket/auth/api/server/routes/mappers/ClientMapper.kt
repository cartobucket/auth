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

package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.api.dto.ClientRequest
import com.cartobucket.auth.api.dto.ClientResponse
import com.cartobucket.auth.data.domain.Client
import java.net.URI

object ClientMapper {

    @JvmStatic
    fun to(clientRequest: ClientRequest): Client {
        val client = Client()
        client.name = clientRequest.name
        client.authorizationServerId = clientRequest.authorizationServerId
        client.redirectUris = clientRequest.redirectUris?.map { URI.create(it) } ?: emptyList()
        client.scopes = clientRequest.scopes?.map { Scope(it) } ?: emptyList()
        client.metadata = MetadataMapper.from(clientRequest.metadata)
        return client
    }

    @JvmStatic
    fun toResponse(client: Client): ClientResponse {
        return ClientResponse().apply {
            id = client.id.toString()
            name = client.name
            authorizationServerId = client.authorizationServerId
            redirectUris = client.redirectUris?.map { it.toString() } ?: emptyList()
            scopes = client.scopes?.map { ScopeMapper.toResponse(it) } ?: emptyList()
            metadata = MetadataMapper.to(client.metadata)
            createdOn = client.createdOn
            updatedOn = client.updatedOn
        }
    }
}
