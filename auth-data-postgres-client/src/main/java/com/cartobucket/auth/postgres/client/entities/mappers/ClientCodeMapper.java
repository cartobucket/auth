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

package com.cartobucket.auth.postgres.client.entities.mappers;

import com.cartobucket.auth.data.domain.ClientCode;
import com.cartobucket.auth.data.domain.Scope;

import java.util.Collections;
import java.util.UUID;

public class ClientCodeMapper {
    public static ClientCode from (com.cartobucket.auth.postgres.client.entities.ClientCode clientCode, com.cartobucket.auth.data.services.ScopeService scopeService) {
        var _clientCode = new ClientCode();
        _clientCode.setCode(clientCode.getCode());
        _clientCode.setClientId(String.valueOf(clientCode.getClientId()));
        _clientCode.setCodeChallenge(clientCode.getCodeChallenge());
        _clientCode.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        _clientCode.setId(clientCode.getId());
        _clientCode.setNonce(clientCode.getNonce());
        _clientCode.setState(clientCode.getState());
        _clientCode.setAuthorizationServerId(clientCode.getAuthorizationServerId());
        _clientCode.setRedirectUri(clientCode.getRedirectUri());
        _clientCode.setUserId(clientCode.getUserId());
        // Convert scope IDs back to Scope entities
        if (clientCode.getScopeIds() != null && !clientCode.getScopeIds().isEmpty()) {
            var scopes = clientCode.getScopeIds().stream()
                    .filter(scopeId -> scopeId != null) // Filter out null scope IDs
                    .map(scopeId -> {
                        try {
                            return scopeService.getScope(scopeId);
                        } catch (Exception e) {
                            // Log the error but don't fail the entire operation
                            System.err.println("Failed to load scope with ID: " + scopeId + " - " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(scope -> scope != null) // Filter out failed scope lookups
                    .toList();
            _clientCode.setScopes(scopes);
        } else {
            _clientCode.setScopes(Collections.emptyList());
        }
        _clientCode.setCreatedOn(clientCode.getCreatedOn());
        return _clientCode;
    }

    public static com.cartobucket.auth.postgres.client.entities.ClientCode to (ClientCode clientCode) {
        var _clientCode = new com.cartobucket.auth.postgres.client.entities.ClientCode();
        _clientCode.setCode(clientCode.getCode());
        _clientCode.setClientId(UUID.fromString(clientCode.getClientId()));
        _clientCode.setCodeChallenge(clientCode.getCodeChallenge());
        _clientCode.setCodeChallengeMethod(clientCode.getCodeChallengeMethod());
        _clientCode.setId(clientCode.getId());
        _clientCode.setNonce(clientCode.getNonce());
        _clientCode.setState(clientCode.getState());
        _clientCode.setAuthorizationServerId(clientCode.getAuthorizationServerId());
        _clientCode.setRedirectUri(clientCode.getRedirectUri());
        _clientCode.setUserId(clientCode.getUserId());
        if (clientCode.getScopes() != null) {
            _clientCode.setScopeIds(clientCode.getScopes().stream()
                    .map(Scope::getId)
                    .toList());
        } else {
            _clientCode.setScopeIds(Collections.emptyList());
        }
        _clientCode.setCreatedOn(clientCode.getCreatedOn());
        return _clientCode;
    }
}
