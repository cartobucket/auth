package com.revethq.auth.web.api.routes

import com.revethq.auth.core.api.dto.Page
import java.util.UUID

object Pagination {
    @JvmStatic
    fun getPage(root: String, authorizationServerIds: List<UUID>, limit: Int, offset: Int): Page {
        val page = Page()
        val authorizationServersParams = authorizationServerIds
            .joinToString("") { "&authorizationServerId=$it" }
        var next = "/$root?limit$limit&offset=${offset + limit}"
        if (authorizationServersParams.isNotEmpty()) {
            next += authorizationServersParams
        }
        page.next = next
        if (offset > 0) {
            var previous = "/$root?limit$limit&offset=${offset - limit}"
            if (authorizationServersParams.isNotEmpty()) {
                previous += authorizationServersParams
            }
            page.previous = previous
        }
        return page
    }
}
