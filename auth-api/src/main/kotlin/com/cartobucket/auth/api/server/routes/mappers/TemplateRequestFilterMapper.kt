// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.TemplateRequestFilter
import java.util.UUID

object TemplateRequestFilterMapper {
    fun to(authorizationServerIds: List<UUID>): TemplateRequestFilter =
        TemplateRequestFilter(
            authorizationServerIds = authorizationServerIds,
        )
}
