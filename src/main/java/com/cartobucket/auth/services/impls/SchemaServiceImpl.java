package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.SchemaRequest;
import com.cartobucket.auth.model.generated.SchemaRequestFilter;
import com.cartobucket.auth.model.generated.SchemaResponse;
import com.cartobucket.auth.model.generated.SchemasResponse;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.cartobucket.auth.models.mappers.SchemaMapper;
import com.cartobucket.auth.repositories.SchemaRepository;
import com.cartobucket.auth.services.SchemaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.time.OffsetDateTime;
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
    public SchemaResponse createSchema(SchemaRequest schemaRequest) {
        var schema = SchemaMapper.to(schemaRequest);
        schema.setCreatedOn(OffsetDateTime.now());
        schema.setUpdatedOn(OffsetDateTime.now());
        schema = schemaRepository.save(schema);
        return SchemaMapper.toResponse(schema);
    }

    @Override
    public void deleteSchema(UUID schemaId) {
        var schema = schemaRepository
                .findById(schemaId)
                .orElseThrow(NotFoundException::new);
        schemaRepository.delete(schema);
    }

    @Override
    public SchemaResponse getSchema(UUID schemaId) {
        var schema = schemaRepository
                .findById(schemaId)
                .orElseThrow(NotFoundException::new);
        return SchemaMapper.toResponse(schema);
    }

    @Override
    public SchemasResponse getSchemas(SchemaRequestFilter filter) {
        var schemaStream = StreamSupport.stream(schemaRepository.findAll().spliterator(), false);
        // TODO: This should happen in the DB.
        if (!filter.getAuthorizationServerIds().isEmpty()) {
            schemaStream = schemaStream.filter(scope -> filter.getAuthorizationServerIds().contains(scope.getAuthorizationServerId()));
        }

        var schemasResponse = new SchemasResponse();
        schemasResponse.setSchemas(schemaStream.map(SchemaMapper::toResponse).toList());
        return schemasResponse;
    }

    @Override
    public SchemaResponse updateSchema(UUID schemaId, SchemaRequest schemaRequest) {
        var schema = schemaRepository
                .findById(schemaId)
                .orElseThrow(NotFoundException::new);
        var _schema = SchemaMapper.to(schemaRequest);
        schema.setJsonSchemaVersion(_schema.getJsonSchemaVersion());
        schema.setName(_schema.getName());
        schema.setSchema(_schema.getSchema());
        schema.setUpdatedOn(OffsetDateTime.now());
        schemaRepository.save(schema);
        return SchemaMapper.toResponse(schema);
    }
}
