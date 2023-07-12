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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.AuthorizationServer;
import com.cartobucket.auth.data.domain.Client;
import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.PasswordAuthRequest;
import com.cartobucket.auth.data.exceptions.badrequests.CodeChallengeBadData;
import com.cartobucket.auth.data.exceptions.notfound.ClientNotFound;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class ClientService implements com.cartobucket.auth.data.services.ClientService {

    @Override
    public ClientCode buildClientCodeForEmailAndPassword(AuthorizationServer authorizationServer, UUID clientId, String scopes, String redirectUri, String nonce, String state, String codeChallenge, String codeChallengeMethod, PasswordAuthRequest userAuthorizationRequest) throws CodeChallengeBadData {
        return null;
    }

    @Override
    public void deleteClient(UUID clientId) {

    }

    @Override
    public Client getClient(UUID clientId) throws ClientNotFound {
        return null;
    }

    @Override
    public Client updateClient(UUID clientId, Client client) throws ClientNotFound {
        return null;
    }

    @Override
    public List<Client> getClients(List<UUID> authorizationServerIds) {
        return null;
    }

    @Override
    public Client createClient(Client client) {
        return null;
    }
}
