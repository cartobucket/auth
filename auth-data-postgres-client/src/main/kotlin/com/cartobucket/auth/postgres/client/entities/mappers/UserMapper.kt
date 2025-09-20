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

package com.cartobucket.auth.postgres.client.entities.mappers

import com.cartobucket.auth.data.domain.User

object UserMapper {
    fun from(user: com.cartobucket.auth.postgres.client.entities.User): User {
        val domainUser = User()
        domainUser.id = user.id
        domainUser.updatedOn = user.updatedOn
        domainUser.createdOn = user.createdOn
        domainUser.authorizationServerId = user.authorizationServerId
        domainUser.metadata = user.metadata
        domainUser.email = user.email
        domainUser.username = user.username
        domainUser.password = user.passwordHash
        return domainUser
    }

    fun to(user: User): com.cartobucket.auth.postgres.client.entities.User {
        val entityUser =
            com.cartobucket.auth.postgres.client.entities
                .User()
        entityUser.id = user.id
        entityUser.updatedOn = user.updatedOn
        entityUser.createdOn = user.createdOn
        entityUser.metadata = user.metadata
        entityUser.authorizationServerId = user.authorizationServerId
        entityUser.email = user.email
        entityUser.username = user.username
        entityUser.passwordHash = user.password
        return entityUser
    }
}
