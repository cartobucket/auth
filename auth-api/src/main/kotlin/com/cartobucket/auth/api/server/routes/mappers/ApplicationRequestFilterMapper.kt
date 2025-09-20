// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ApplicationRequestFilter
import java.util.UUID

object ApplicationRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): ApplicationRequestFilter =
        ApplicationRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
