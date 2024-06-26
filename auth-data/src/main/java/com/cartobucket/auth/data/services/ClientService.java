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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientCodeNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.ClientCode;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    Client createClient(Client client);

    Client getClient(String clientId) throws ClientNotFound;

    List<Client> getClients(List<UUID> authorizationServerIds, Page page);

    void deleteClient(UUID clientId) throws ClientNotFound;

    Client updateClient(UUID clientId, Client client) throws ClientNotFound;

    ClientCode createClientCode(
            UUID authorizationServerId,
            ClientCode clientCode
    );

    ClientCode getClientCode(String clientCode) throws ClientCodeNotFound;
}
