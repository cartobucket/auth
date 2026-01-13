/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.core.services

import com.revethq.auth.core.domain.Pair
import com.revethq.auth.core.domain.Profile
import com.revethq.core.Identifier
import com.revethq.core.SchemaValidation
import com.revethq.auth.core.domain.User
import com.revethq.auth.core.exceptions.notfound.ProfileNotFound
import com.revethq.auth.core.exceptions.notfound.UserNotFound
import java.util.*

interface UserService {
    data class Page(
        val limit: Int,
        val offset: Int
    ) {
        fun offset(): Int {
            return offset
        }

        fun limit(): Int {
            return limit
        }
    }

    data class UserQuery(
        val authorizationServerIds: List<UUID>,
        val userIds: List<UUID>,
        val emails: List<String>,
        val identifiers: List<Identifier>,
        val schemaValidations: List<SchemaValidation>,
        val page: Page
    ) {
        fun userIds(): List<UUID> {
            return userIds
        }

        fun emails(): List<String> {
            return emails
        }

        fun identifiers(): List<Identifier> {
            return identifiers
        }

        fun schemaValidations(): List<SchemaValidation> {
            return schemaValidations
        }

        fun page(): Page {
            return page
        }

        fun authorizationServerIds(): List<UUID> {
            return authorizationServerIds
        }
    }

    fun query(query: UserQuery): List<User>

    fun getUsers(authorizationServerIds: List<UUID>, page: com.revethq.auth.core.domain.Page): List<User>

    fun createUser(userProfilePair: Pair<User, Profile>): Pair<User, Profile>

    fun deleteUser(userId: UUID)

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun getUser(userId: UUID): Pair<User, Profile>

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun getUser(username: String): Pair<User, Profile>

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun updateUser(userId: UUID, userProfilePair: Pair<User, Profile>): Pair<User, Profile>

    fun setPassword(user: User, password: String)

    fun validatePassword(userId: UUID, password: String): Boolean
}