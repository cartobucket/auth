// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.SchemaRequestFilter
import java.util.UUID

object SchemaRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): SchemaRequestFilter =
        SchemaRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
