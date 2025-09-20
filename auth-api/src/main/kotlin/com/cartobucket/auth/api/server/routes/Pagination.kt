// (C)2024
package com.cartobucket.auth.api.server.routes

import java.util.UUID

object Pagination {
    fun getPage(
        root: String,
        authorizationServerIds: List<UUID>,
        limit: Int,
        offset: Int,
    ): com.cartobucket.auth.api.dto.Page {
        val authorizationServersParams =
            authorizationServerIds
                .joinToString("") { "&authorizationServerId=$it" }

        var next = "/$root?limit=$limit&offset=${offset + limit}"
        if (authorizationServersParams.isNotEmpty()) {
            next += authorizationServersParams
        }

        val previous =
            if (offset > 0) {
                var prev = "/$root?limit=$limit&offset=${offset - limit}"
                if (authorizationServersParams.isNotEmpty()) {
                    prev += authorizationServersParams
                }
                prev
            } else {
                null
            }

        return com.cartobucket.auth.api.dto.Page(
            next = next,
            previous = previous,
        )
    }
}
