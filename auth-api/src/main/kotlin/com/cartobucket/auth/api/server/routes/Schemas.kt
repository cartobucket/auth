// (C)2024
package com.cartobucket.auth.api.server.routes

import com.cartobucket.auth.api.dto.SchemaRequest
import com.cartobucket.auth.api.dto.SchemasResponse
import com.cartobucket.auth.api.interfaces.SchemasApi
import com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT
import com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT
import com.cartobucket.auth.api.server.routes.Pagination.getPage
import com.cartobucket.auth.api.server.routes.mappers.SchemaMapper
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.services.SchemaService
import jakarta.ws.rs.core.Response
import java.util.UUID

class Schemas(
    private val schemaService: SchemaService,
) : SchemasApi {
    override fun createSchema(schemaRequest: SchemaRequest): Response =
        Response
            .ok()
            .entity(
                SchemaMapper.toResponse(
                    schemaService.createSchema(SchemaMapper.to(schemaRequest)),
                ),
            ).build()

    override fun deleteSchema(schemaId: UUID): Response {
        schemaService.deleteSchema(schemaId)
        return Response.noContent().build()
    }

    override fun getSchema(schemaId: UUID): Response =
        Response
            .ok()
            .entity(SchemaMapper.toResponse(schemaService.getSchema(schemaId)))
            .build()

    override fun listSchemas(
        authorizationServerIds: List<UUID>?,
        limit: Int?,
        offset: Int?,
    ): Response {
        val actualLimit = limit ?: LIMIT_DEFAULT
        val actualOffset = offset ?: OFFSET_DEFAULT
        val actualAuthorizationServerIds = authorizationServerIds ?: emptyList()

        val schemas =
            schemaService
                .getSchemas(actualAuthorizationServerIds, Page(actualLimit, actualOffset))
                .map { SchemaMapper.toResponse(it) }
        val page = getPage("schemas", actualAuthorizationServerIds, actualLimit, actualOffset)
        val schemasResponse =
            SchemasResponse(
                schemas = schemas,
                page = page,
            )
        return Response
            .ok()
            .entity(schemasResponse)
            .build()
    }

    override fun updateSchema(
        schemaId: UUID,
        schemaRequest: SchemaRequest,
    ): Response =
        Response
            .ok()
            .entity(
                SchemaMapper.toResponse(
                    schemaService.updateSchema(schemaId, SchemaMapper.to(schemaRequest)),
                ),
            ).build()
}
