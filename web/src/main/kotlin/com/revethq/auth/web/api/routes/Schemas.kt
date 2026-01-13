/*
 * Copyright 2023 Bryce Groff (Revet)
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
package com.revethq.auth.web.api.routes

import com.revethq.auth.core.api.dto.SchemaRequest
import com.revethq.auth.core.api.dto.SchemasResponse
import com.revethq.auth.core.api.interfaces.SchemasApi
import com.revethq.auth.web.api.routes.Constants.LIMIT_DEFAULT
import com.revethq.auth.web.api.routes.Constants.OFFSET_DEFAULT
import com.revethq.auth.web.api.routes.Pagination.getPage
import com.revethq.auth.web.api.routes.mappers.SchemaMapper
import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.services.SchemaService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.UUID

@ApplicationScoped
open class Schemas @Inject constructor(
    private val schemaService: SchemaService
) : SchemasApi {

    override fun createSchema(schemaRequest: SchemaRequest): Response {
        return Response
            .ok()
            .entity(
                SchemaMapper.toResponse(
                    schemaService.createSchema(SchemaMapper.to(schemaRequest))
                )
            )
            .build()
    }

    override fun deleteSchema(schemaId: UUID): Response {
        schemaService.deleteSchema(schemaId)
        return Response.noContent().build()
    }

    override fun getSchema(schemaId: UUID): Response {
        return Response
            .ok()
            .entity(SchemaMapper.toResponse(schemaService.getSchema(schemaId)))
            .build()
    }

    override fun listSchemas(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val serverIds = authorizationServerIds ?: emptyList()

        val schemasResponse = SchemasResponse()
        schemasResponse.schemas = schemaService
            .getSchemas(serverIds, Page(actualLimit, actualOffset))
            .map { SchemaMapper.toResponse(it) }
        schemasResponse.page = getPage("schemas", serverIds, actualLimit, actualOffset)
        return Response.ok().entity(schemasResponse).build()
    }

    override fun updateSchema(schemaId: UUID, schemaRequest: SchemaRequest): Response {
        return Response
            .ok()
            .entity(
                SchemaMapper.toResponse(
                    schemaService.updateSchema(schemaId, SchemaMapper.to(schemaRequest))
                )
            )
            .build()
    }
}
