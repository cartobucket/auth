// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.SchemaRequest
import com.cartobucket.auth.api.dto.SchemaResponse
import com.cartobucket.auth.data.domain.Schema

object SchemaMapper {
    fun toResponse(schema: Schema): SchemaResponse =
        SchemaResponse(
            schema = schema.schema,
            schemaVersion = schema.jsonSchemaVersion,
            id = schema.id,
            name = schema.name,
            authorizationServer = schema.authorizationServerId!!, // Required field
            metadata = MetadataMapper.to(schema.metadata),
        )

    fun to(schemaRequest: SchemaRequest): Schema {
        val schema = Schema()
        schema.schema = schemaRequest.schema as? Map<String, Any>
        schema.jsonSchemaVersion = schemaRequest.schemaVersion
        schema.name = schemaRequest.name
        schema.authorizationServerId = schemaRequest.authorizationServer
        schema.metadata = MetadataMapper.from(schemaRequest.metadata)
        return schema
    }
}
