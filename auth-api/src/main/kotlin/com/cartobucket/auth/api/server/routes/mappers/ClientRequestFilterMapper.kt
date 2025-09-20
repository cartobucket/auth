// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ClientRequestFilter
import java.util.UUID

object ClientRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): ClientRequestFilter =
        ClientRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
