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
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.UserRequest
import com.cartobucket.auth.api.dto.UsersResponse
import com.cartobucket.auth.api.interfaces.UsersApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.UserMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.services.UserService
import jakarta.ws.rs.core.Response
import java.util.UUID

open class Users(
    private val userService: UserService
) : UsersApi {

    override fun createUser(userRequest: UserRequest): Response {
        return Response
            .ok()
            .entity(
                UserMapper.toResponse(
                    userService.createUser(UserMapper.from(userRequest))
                )
            )
            .build()
    }

    override fun deleteUser(userId: UUID): Response {
        userService.deleteUser(userId)
        return Response.noContent().build()
    }

    override fun getUser(userId: UUID): Response {
        return Response
            .ok()
            .entity(UserMapper.toResponse(userService.getUser(userId)))
            .build()
    }

    override fun listUsers(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val usersResponse = UsersResponse()
        usersResponse.users = userService
            .getUsers(serverIds, Page(actualLimit, actualOffset))
            .map { user -> UserMapper.toResponse(Pair.create(user, Profile())) }
        usersResponse.page = getPage("users", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(usersResponse).build()
    }

    override fun updateUser(userId: UUID, userRequest: UserRequest): Response {
        return Response
            .ok()
            .entity(
                UserMapper.toResponse(
                    userService.updateUser(userId, UserMapper.from(userRequest))
                )
            )
            .build()
    }
}
