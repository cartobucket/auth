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

import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.exceptions.notfound.ScopeNotFound
import java.util.UUID

interface ScopeService {
    fun getScopes(
        authorizationServerIds: List<UUID>,
        page: Page,
    ): List<Scope>

    fun createScope(scope: Scope): Scope

    @Throws(ScopeNotFound::class)
    fun deleteScope(scopeId: UUID)

    @Throws(ScopeNotFound::class)
    fun getScope(scopeId: UUID): Scope

    // Takes in a list of scopes and filters those scopes based on what scopes are associated with the AS.
    fun filterScopesForAuthorizationServerId(
        authorizationServerId: UUID,
        scopes: String,
    ): List<Scope>

    fun getScopesForResourceId(id: UUID): List<Scope>

    companion object {
        @JvmStatic
        fun scopeStringToScopeList(scopes: String?): List<Scope> {
            if (scopes == null) {
                return emptyList()
            }

            val splitScopes = scopes.split("\\p{Zs}+".toRegex())

            return splitScopes.map { scopeName ->
                Scope().apply { name = scopeName }
            }
        }

        @JvmStatic
        fun scopeListToScopeString(scopes: List<String>): String = scopes.joinToString(" ")

        @JvmStatic
        fun filterScopesByList(
            scopes: String?,
            authorizationServerScopes: List<String>?,
        ): List<Scope> {
            if (scopes == null || authorizationServerScopes == null) {
                return emptyList()
            }
            return scopeStringToScopeList(scopes)
                .map { it.name }
                .filter { authorizationServerScopes.contains(it) }
                .map { scopeName ->
                    Scope().apply { name = scopeName }
                }
        }
    }
}
