package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.SchemaRequest;
import com.cartobucket.auth.model.generated.SchemaRequestFilter;
import com.cartobucket.auth.model.generated.SchemaResponse;
import com.cartobucket.auth.model.generated.SchemasResponse;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.networknt.schema.ValidationMessage;

import java.util.Set;
import java.util.UUID;

public interface SchemaService {
    Set<ValidationMessage> validateProfileAgainstSchema(Profile profile, Schema schema);
    SchemaResponse createSchema(SchemaRequest schemaRequest);

    void deleteSchema(UUID schemaId);

    SchemaResponse getSchema(UUID schemaId);

    SchemasResponse getSchemas(SchemaRequestFilter authorizationServer);

    SchemaResponse updateSchema(UUID schemaId, SchemaRequest schemaRequest);


}
