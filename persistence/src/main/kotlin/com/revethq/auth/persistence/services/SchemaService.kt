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

package com.revethq.auth.persistence.services

import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.Profile
import com.revethq.auth.core.domain.Schema
import com.revethq.auth.core.exceptions.notfound.SchemaNotFound
import com.revethq.auth.persistence.repositories.EventRepository
import com.revethq.auth.persistence.repositories.SchemaRepository
import com.revethq.auth.persistence.entities.EventType
import com.revethq.auth.persistence.entities.mappers.SchemaMapper
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.json.bind.JsonbBuilder
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.StringReader
import java.time.OffsetDateTime
import java.util.UUID

@ApplicationScoped
class SchemaService(
    private val eventRepository: EventRepository,
    private val schemaRepository: SchemaRepository
) : com.revethq.auth.core.services.SchemaService {

    companion object {
        private val LOG = Logger.getLogger(SchemaService::class.java)
    }

    private val jsonValidationService: JsonValidationService = JsonValidationService.newInstance()

    override fun validateProfileAgainstSchema(profile: Profile, schema: Schema): Set<String> {
        val errors = mutableSetOf<String>()

        if (profile.profile == null) {
            return errors // Nothing to validate
        }

        if (schema.schema == null) {
            return errors // No schema to validate against
        }

        try {
            // Convert schema Map to JSON string, then parse as JsonSchema
            val jsonb = JsonbBuilder.create()
            val schemaJson = jsonb.toJson(schema.schema)
            val jsonSchema = jsonValidationService.readSchema(StringReader(schemaJson))

            // Convert profile Map to JSON string
            val profileJson = jsonb.toJson(profile.profile)

            // Create a problem handler that collects errors
            val problems = mutableListOf<org.leadpony.justify.api.Problem>()
            val handler = ProblemHandler.collectingTo(problems)

            // Validate the profile against the schema
            jsonValidationService.createReader(
                StringReader(profileJson),
                jsonSchema,
                handler
            ).use { reader ->
                reader.readValue()
            }

            // Convert Problem objects to error messages
            for (problem in problems) {
                errors.add(problem.message)
            }
        } catch (e: Exception) {
            LOG.warn("Error validating profile against schema: ${e.message}", e)
            errors.add("Schema validation error: ${e.message}")
        }

        return errors
    }

    @Transactional
    override fun createSchema(schema: Schema): Schema {
        schema.createdOn = OffsetDateTime.now()
        schema.updatedOn = OffsetDateTime.now()
        val _schema = SchemaMapper.to(schema)
        schemaRepository.persist(_schema)
        eventRepository.createSchemaEvent(SchemaMapper.from(_schema), EventType.CREATE)
        return SchemaMapper.from(_schema)
    }

    @Transactional
    @Throws(SchemaNotFound::class)
    override fun deleteSchema(schemaId: UUID) {
        val schema = schemaRepository
            .findByIdOptional(schemaId)
            .orElseThrow { SchemaNotFound() }
        schemaRepository.delete(schema)
        eventRepository.createSchemaEvent(SchemaMapper.from(schema), EventType.DELETE)
    }

    @Transactional
    @Throws(SchemaNotFound::class)
    override fun getSchema(schemaId: UUID): Schema {
        return schemaRepository
            .findByIdOptional(schemaId)
            .map { SchemaMapper.from(it) }
            .orElseThrow { SchemaNotFound() }
    }

    @Transactional
    override fun getSchemas(authorizationServerIds: List<UUID>, page: Page): List<Schema> {
        val allEntities: List<com.revethq.auth.persistence.entities.Schema> = if (authorizationServerIds.isNotEmpty()) {
            schemaRepository.list("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
        } else {
            schemaRepository.listAll(Sort.descending("createdOn"))
        }
        val fromIndex = page.offset().coerceAtMost(allEntities.size)
        val toIndex = (page.offset() + page.limit()).coerceAtMost(allEntities.size)
        return allEntities.subList(fromIndex, toIndex).map { SchemaMapper.from(it) }
    }

    override fun getSchemaByNameAndAuthorizationServerId(name: String, authorizationServerId: UUID): Schema? {
        return schemaRepository.findByNameAndAuthorizationServerId(name, authorizationServerId)
            .map { SchemaMapper.from(it) }
            .orElse(null)
    }

    @Transactional
    @Throws(SchemaNotFound::class)
    override fun updateSchema(schemaId: UUID, schema: Schema): Schema {
        val _schema = schemaRepository
            .findByIdOptional(schemaId)
            .orElseThrow { SchemaNotFound() }
        _schema.jsonSchemaVersion = schema.jsonSchemaVersion
        _schema.name = schema.name
        _schema.schema = schema.schema
        _schema.updatedOn = OffsetDateTime.now()
        schemaRepository.persist(_schema)
        eventRepository.createSchemaEvent(SchemaMapper.from(_schema), EventType.UPDATE)
        return SchemaMapper.from(_schema)
    }
}
