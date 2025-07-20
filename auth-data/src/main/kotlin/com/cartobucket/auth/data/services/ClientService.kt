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

package com.cartobucket.auth.data.services

import com.cartobucket.auth.data.domain.Client
import com.cartobucket.auth.data.domain.ClientCode
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.exceptions.notfound.ClientCodeNotFound
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound
import java.util.UUID

interface ClientService {
    fun createClient(client: Client): Client

    @Throws(ClientNotFound::class)
    fun getClient(clientId: String): Client

    fun getClients(authorizationServerIds: List<UUID>, page: Page): List<Client>

    @Throws(ClientNotFound::class)
    fun deleteClient(clientId: UUID)

    @Throws(ClientNotFound::class)
    fun updateClient(clientId: UUID, client: Client): Client

    fun createClientCode(authorizationServerId: UUID, clientCode: ClientCode): ClientCode

    @Throws(ClientCodeNotFound::class)
    fun getClientCode(clientCode: String): ClientCode
}