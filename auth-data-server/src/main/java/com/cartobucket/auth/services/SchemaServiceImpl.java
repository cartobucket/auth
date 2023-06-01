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

package com.cartobucket.auth.services;

import com.cartobucket.auth.exceptions.notfound.SchemaNotFound;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.cartobucket.auth.repositories.SchemaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class SchemaServiceImpl implements SchemaService {
    final SchemaRepository schemaRepository;
    final ObjectMapper objectMapper = new ObjectMapper();

    public SchemaServiceImpl(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @Override
    public Set<ValidationMessage> validateProfileAgainstSchema(Profile profile, Schema schema) {
        try {
            var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
            var jsonSchema = factory.getSchema(objectMapper.writeValueAsString(schema.getSchema()));
            return jsonSchema.validate(
                    objectMapper.convertValue(profile.getProfile(), JsonNode.class)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Schema createSchema(final Schema schema) {
        schema.setCreatedOn(OffsetDateTime.now());
        schema.setUpdatedOn(OffsetDateTime.now());
        return schemaRepository.save(schema);
    }

    @Override
    public void deleteSchema(final UUID schemaId) {
        var schema = schemaRepository
                .findById(schemaId)
                .orElseThrow(SchemaNotFound::new);
        schemaRepository.delete(schema);
    }

    @Override
    public Schema getSchema(final UUID schemaId) {
        return schemaRepository
                .findById(schemaId)
                .orElseThrow(SchemaNotFound::new);
    }

    @Override
    public List<Schema> getSchemas(final List<UUID> authorizationServerIds) {
        if (!authorizationServerIds.isEmpty()) {
            return schemaRepository.findAllByAuthorizationServerIdIn(authorizationServerIds);
        } else {
            return StreamSupport
                    .stream(schemaRepository.findAll().spliterator(), false)
                    .toList();
        }
    }

    @Override
    public Schema updateSchema(final UUID schemaId, Schema schema) {
        var _schema = schemaRepository
                .findById(schemaId)
                .orElseThrow(SchemaNotFound::new);
        _schema.setJsonSchemaVersion(_schema.getJsonSchemaVersion());
        _schema.setName(_schema.getName());
        _schema.setSchema(_schema.getSchema());
        _schema.setUpdatedOn(OffsetDateTime.now());
        return schemaRepository.save(schema);
    }
}
