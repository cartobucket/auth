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

import com.cartobucket.auth.data.domain.Identifier
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.SchemaValidation
import com.cartobucket.auth.data.domain.User
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound
import com.cartobucket.auth.data.exceptions.notfound.UserNotFound
import java.util.UUID

interface UserService {
    data class Page(
        val limit: Int,
        val offset: Int,
    ) {
        fun offset(): Int = offset

        fun limit(): Int = limit
    }

    data class UserQuery(
        val authorizationServerIds: List<UUID>,
        val userIds: List<UUID>,
        val emails: List<String>,
        val identifiers: List<Identifier>,
        val schemaValidations: List<SchemaValidation>,
        val page: Page,
    ) {
        fun userIds(): List<UUID> = userIds

        fun emails(): List<String> = emails

        fun identifiers(): List<Identifier> = identifiers

        fun schemaValidations(): List<SchemaValidation> = schemaValidations

        fun page(): Page = page

        fun authorizationServerIds(): List<UUID> = authorizationServerIds
    }

    fun query(query: UserQuery): List<User>

    fun getUsers(
        authorizationServerIds: List<UUID>,
        page: com.cartobucket.auth.data.domain.Page,
    ): List<User>

    fun createUser(userProfilePair: Pair<User, Profile>): Pair<User, Profile>

    fun deleteUser(userId: UUID)

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun getUser(userId: UUID): Pair<User, Profile>

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun getUser(username: String): Pair<User, Profile>

    @Throws(UserNotFound::class, ProfileNotFound::class)
    fun updateUser(
        userId: UUID,
        userProfilePair: Pair<User, Profile>,
    ): Pair<User, Profile>

    fun setPassword(
        user: User,
        password: String,
    )

    fun validatePassword(
        userId: UUID,
        password: String,
    ): Boolean
}
