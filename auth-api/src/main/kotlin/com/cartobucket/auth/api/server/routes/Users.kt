// (C)2024
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

class Users(
    private val userService: UserService,
) : UsersApi {
    override fun createUser(userRequest: UserRequest): Response =
        Response
            .ok()
            .entity(
                UserMapper.toResponse(
                    userService.createUser(UserMapper.from(userRequest)),
                ),
            ).build()

    override fun deleteUser(userId: UUID): Response {
        userService.deleteUser(userId)
        return Response.noContent().build()
    }

    override fun getUser(userId: UUID): Response =
        Response
            .ok()
            .entity(
                UserMapper.toResponse(userService.getUser(userId)),
            ).build()

    override fun listUsers(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val users =
            userService
                .getUsers(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { user -> UserMapper.toResponse(Pair.create(user, Profile())) }
        val page = getPage("users", actualAuthorizationServerIds, actualLimit, actualOffset)
        val usersResponse =
            UsersResponse(
                users = users,
                page = page,
            )
        return Response
            .ok()
            .entity(usersResponse)
            .build()
    }

    override fun updateUser(
        userId: UUID,
        userRequest: UserRequest,
    ): Response =
        Response
            .ok()
            .entity(
                UserMapper.toResponse(
                    userService.updateUser(userId, UserMapper.from(userRequest)),
                ),
            ).build()
}
