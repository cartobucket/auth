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

package com.cartobucket.auth.rpc.server.entities.mappers;

import com.cartobucket.auth.data.domain.ClientCode;

public class ClientCodeMapper {
    public static ClientCode from (com.cartobucket.auth.rpc.server.entities.ClientCode clientCode) {
        var _clientCode = new ClientCode();
        _clientCode.setCode(clientCode.getCode());
        _clientCode.setClientId(clientCode.getClientId());
        _clientCode.setCodeChallenge(clientCode.getCodeChallenge());
        _clientCode.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        _clientCode.setId(clientCode.getId());
        _clientCode.setScopes(clientCode.getScopes());
        _clientCode.setNonce(clientCode.getNonce());
        _clientCode.setState(clientCode.getState());
        _clientCode.setScopes(clientCode.getScopes());
        _clientCode.setAuthorizationServerId(clientCode.getAuthorizationServerId());
        _clientCode.setRedirectUri(clientCode.getRedirectUri());
        _clientCode.setUserId(clientCode.getUserId());
        _clientCode.setCreatedOn(clientCode.getCreatedOn());
        return _clientCode;
    }

    public static com.cartobucket.auth.rpc.server.entities.ClientCode to (ClientCode clientCode) {
        var _clientCode = new com.cartobucket.auth.rpc.server.entities.ClientCode();
        _clientCode.setCode(clientCode.getCode());
        _clientCode.setClientId(clientCode.getClientId());
        _clientCode.setCodeChallenge(clientCode.getCodeChallenge());
        _clientCode.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        _clientCode.setId(clientCode.getId());
        _clientCode.setScopes(clientCode.getScopes());
        _clientCode.setNonce(clientCode.getNonce());
        _clientCode.setState(clientCode.getState());
        _clientCode.setScopes(clientCode.getScopes());
        _clientCode.setAuthorizationServerId(clientCode.getAuthorizationServerId());
        _clientCode.setRedirectUri(clientCode.getRedirectUri());
        _clientCode.setUserId(clientCode.getUserId());
        _clientCode.setCreatedOn(clientCode.getCreatedOn());
        return _clientCode;
    }
}