// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.UserRequestFilter
import java.util.UUID

object UserRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): UserRequestFilter =
        UserRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
