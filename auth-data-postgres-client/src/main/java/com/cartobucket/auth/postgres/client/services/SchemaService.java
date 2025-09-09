/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
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

package com.cartobucket.auth.postgres.client.services;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.exceptions.notfound.SchemaNotFound;
import com.cartobucket.auth.postgres.client.repositories.EventRepository;
import com.cartobucket.auth.postgres.client.repositories.SchemaRepository;
import com.cartobucket.auth.postgres.client.entities.EventType;
import com.cartobucket.auth.postgres.client.entities.mappers.SchemaMapper;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class SchemaService implements com.cartobucket.auth.data.services.SchemaService {
    final EventRepository eventRepository;
    final SchemaRepository schemaRepository;

    public SchemaService(
            EventRepository eventRepository,
            SchemaRepository schemaRepository
    ) {
        this.eventRepository = eventRepository;
        this.schemaRepository = schemaRepository;
    }

    @Override
    public Set<String> validateProfileAgainstSchema(Profile profile, Schema schema) {
        // TODO: Schema validation temporarily disabled to remove Jackson dependency
        // The networknt schema library requires Jackson's JsonNode
        // Need to find an alternative JSON Schema validator that works without Jackson
        return Collections.emptySet(); // Return empty set = no validation errors
    }

    @Override
    @Transactional
    public Schema createSchema(final Schema schema) {
        schema.setCreatedOn(OffsetDateTime.now());
        schema.setUpdatedOn(OffsetDateTime.now());
        var _schema = SchemaMapper.to(schema);
        schemaRepository.persist(_schema);
        eventRepository.createSchemaEvent(SchemaMapper.from(_schema), EventType.CREATE);
        return SchemaMapper.from(_schema);
    }

    @Override
    @Transactional
    public void deleteSchema(final UUID schemaId) throws SchemaNotFound {
        var schema = schemaRepository
                .findByIdOptional(schemaId)
                .orElseThrow(SchemaNotFound::new);
        schemaRepository.delete(schema);
        eventRepository.createSchemaEvent(SchemaMapper.from(schema), EventType.DELETE);
    }

    @Override
    @Transactional
    public Schema getSchema(final UUID schemaId) throws SchemaNotFound {
        return schemaRepository
                .findByIdOptional(schemaId)
                .map(SchemaMapper::from)
                .orElseThrow(SchemaNotFound::new);
    }

    @Override
    @Transactional
    public List<Schema> getSchemas(final List<UUID> authorizationServerIds, Page page) {
        if (!authorizationServerIds.isEmpty()) {
            return schemaRepository
                    .find("authorizationServerId in ?1", Sort.descending("createdOn"), authorizationServerIds)
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(SchemaMapper::from)
                    .toList();
        } else {
            return schemaRepository.findAll(Sort.descending("createdOn"))
                    .range(page.offset(), page.getNextRowsCount())
                    .list()
                    .stream()
                    .map(SchemaMapper::from)
                    .toList();
        }
    }

    @Override
    public Schema getSchemaByNameAndAuthorizationServerId(String name, UUID authorizationServerId) {
        return schemaRepository.findByNameAndAuthorizationServerId(name, authorizationServerId)
                .map(SchemaMapper::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public Schema updateSchema(final UUID schemaId, Schema schema) throws SchemaNotFound {
        var _schema = schemaRepository
                .findByIdOptional(schemaId)
                .orElseThrow(SchemaNotFound::new);
        _schema.setJsonSchemaVersion(schema.getJsonSchemaVersion());
        _schema.setName(schema.getName());
        _schema.setSchema(schema.getSchema());
        _schema.setUpdatedOn(OffsetDateTime.now());
        schemaRepository.persist(_schema);
        eventRepository.createSchemaEvent(SchemaMapper.from(_schema), EventType.UPDATE);
        return SchemaMapper.from(_schema);
    }
}
