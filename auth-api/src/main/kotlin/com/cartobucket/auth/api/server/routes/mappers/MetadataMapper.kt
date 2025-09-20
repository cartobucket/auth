// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.MetadataIdentifiersInner
import com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner
import com.cartobucket.auth.data.domain.Identifier
import com.cartobucket.auth.data.domain.Metadata
import com.cartobucket.auth.data.domain.SchemaValidation

object MetadataMapper {
    fun from(metadata: com.cartobucket.auth.api.dto.Metadata?): Metadata {
        if (metadata == null) {
            return Metadata(
                identifiers = emptyList(),
                schemaValidations = emptyList(),
                properties = emptyMap(),
            )
        }
        return Metadata(
            identifiers = metadata.identifiers?.map { fromIdentifier(it) } ?: emptyList(),
            schemaValidations = metadata.schemaValidations?.map { fromSchemaValidation(it) } ?: emptyList(),
            properties = metadata.properties ?: emptyMap(),
        )
    }

    private fun fromSchemaValidation(metadataSchemaValidationsInner: MetadataSchemaValidationsInner): SchemaValidation =
        SchemaValidation(
            schemaId = metadataSchemaValidationsInner.schemaId,
            isValid = metadataSchemaValidationsInner.isValid ?: false,
            validatedOn = metadataSchemaValidationsInner.validatedOn,
        )

    private fun fromIdentifier(metadataIdentifiersInner: MetadataIdentifiersInner): Identifier =
        Identifier(
            system = metadataIdentifiersInner.system,
            value = metadataIdentifiersInner.value,
        )

    fun to(metadata: Metadata?): com.cartobucket.auth.api.dto.Metadata {
        if (metadata == null) {
            return com.cartobucket.auth.api.dto.Metadata(
                properties = emptyMap(),
                identifiers = emptyList(),
                schemaValidations = emptyList(),
            )
        }
        return com.cartobucket.auth.api.dto.Metadata(
            properties = metadata.properties ?: emptyMap(),
            identifiers = metadata.identifiers?.map { toIdentifier(it) } ?: emptyList(),
            schemaValidations = metadata.schemaValidations?.map { toSchemaValidation(it) } ?: emptyList(),
        )
    }

    private fun toSchemaValidation(schemaValidation: SchemaValidation): com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner =
        com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner(
            schemaId = schemaValidation.schemaId,
            isValid = schemaValidation.isValid,
            validatedOn = schemaValidation.validatedOn,
        )

    private fun toIdentifier(identifier: Identifier): com.cartobucket.auth.api.dto.MetadataIdentifiersInner =
        com.cartobucket.auth.api.dto.MetadataIdentifiersInner(
            system = identifier.system,
            value = identifier.value,
        )
}
