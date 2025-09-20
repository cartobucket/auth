// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.ScopeRequestFilter
import java.util.UUID

object ScopeRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): ScopeRequestFilter =
        ScopeRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
