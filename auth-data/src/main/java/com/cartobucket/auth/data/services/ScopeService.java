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

import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound;
import com.cartobucket.auth.data.domain.Scope;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.stream;

public interface ScopeService {
    List<Scope> getScopes(List<UUID> authorizationServerIds);

    Scope createScope(Scope scope);

    void deleteScope(UUID scopeId) throws ScopeNotFound;

    Scope getScope(UUID scopeId) throws ScopeNotFound;

    // Takes in a list of scopes and filters those scopes based on what scopes are associated with the AS.
    List<String> filterScopesForAuthorizationServerId(UUID authorizationServerId, String scopes);

    static List<String> scopeStringToScopeList(String scopes) {
        if (scopes == null) {
            return Collections.emptyList();
        }
        return stream(scopes.split("\\p{Zs}+"))
                .toList();
    }

    static String scopeListToScopeString(List<String> scopes) {
        return String.join(" ", scopes);
    }
    static List<String> filterScopesByList(String scopes, List<String> authorizationServerScopes) {
        if (scopes == null || authorizationServerScopes == null) {
            return Collections.emptyList();
        }
        return ScopeService.scopeStringToScopeList(scopes)
                .stream()
                .filter(authorizationServerScopes::contains)
                .toList();
    }
}
