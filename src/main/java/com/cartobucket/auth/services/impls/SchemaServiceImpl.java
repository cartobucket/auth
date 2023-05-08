package com.cartobucket.auth.services.impls;

import com.cartobucket.auth.model.generated.TemplateResponse;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.cartobucket.auth.repositories.SchemaRepository;
import com.cartobucket.auth.services.SchemaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SchemaServiceImpl implements SchemaService {
    final SchemaRepository schemaRepository;

    public SchemaServiceImpl(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    @Override
    public Set<ValidationMessage> validateProfileAgainstSchema(Profile profile, Schema schema) {
        var factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        JsonSchema jsonSchema = factory.getSchema(schema.getSchema());
        return jsonSchema.validate(
                new ObjectMapper().convertValue(profile.getProfile(), JsonNode.class)
        );
    }

    @Override
    public TemplateResponse createSchema(Schema schemaRequest) {
        return null;
    }

    @Override
    public void deleteSchema(UUID schemaId) {

    }

    @Override
    public Schema getSchema(UUID schemaId) {
        return null;
    }

    @Override
    public List<Schema> getSchemasForAuthorizationServer(UUID authorizationServer) {
        return null;
    }

    @Override
    public Schema updateSchema(UUID schemaId, Schema schemaRequest) {
        return null;
    }
}
