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

package com.revethq.auth.web.api.routes.mappers

import com.revethq.core.Identifier
import com.revethq.core.Metadata
import com.revethq.core.SchemaValidation
import com.revethq.auth.core.api.dto.MetadataIdentifiersInner
import com.revethq.auth.core.api.dto.MetadataSchemaValidationsInner

object MetadataMapper {

    @JvmStatic
    fun from(metadata: com.revethq.auth.core.api.dto.Metadata?): Metadata {
        if (metadata == null) {
            return Metadata(emptyList(), emptyList(), emptyMap())
        }

        val identifiers = metadata.identifiers?.map { fromIdentifier(it) } ?: emptyList()
        val schemaValidations = metadata.schemaValidations?.map { fromSchemaValidation(it) } ?: emptyList()
        val properties = metadata.properties ?: emptyMap()

        return Metadata(identifiers, schemaValidations, properties)
    }

    private fun fromSchemaValidation(inner: MetadataSchemaValidationsInner): SchemaValidation {
        return SchemaValidation(
            inner.schemaId,
            inner.isValid ?: false,
            inner.validatedOn
        )
    }

    private fun fromIdentifier(inner: MetadataIdentifiersInner): Identifier {
        return Identifier(
            inner.system,
            inner.value
        )
    }

    @JvmStatic
    fun to(metadata: Metadata?): com.revethq.auth.core.api.dto.Metadata {
        val result = com.revethq.auth.core.api.dto.Metadata()
        if (metadata == null) {
            result.identifiers = mutableListOf()
            result.properties = emptyMap()
            result.schemaValidations = mutableListOf()
            return result
        }

        result.identifiers = metadata.identifiers?.map { toIdentifier(it) }?.toMutableList() ?: mutableListOf()
        result.properties = metadata.properties ?: emptyMap()
        result.schemaValidations = metadata.schemaValidations?.map { toSchemaValidation(it) }?.toMutableList() ?: mutableListOf()

        return result
    }

    private fun toSchemaValidation(schemaValidation: SchemaValidation): MetadataSchemaValidationsInner {
        return MetadataSchemaValidationsInner().apply {
            schemaId = schemaValidation.schemaId
            isValid = schemaValidation.isValid
            validatedOn = schemaValidation.validatedOn
        }
    }

    private fun toIdentifier(identifier: Identifier): MetadataIdentifiersInner {
        return MetadataIdentifiersInner().apply {
            system = identifier.system
            value = identifier.value
        }
    }
}
