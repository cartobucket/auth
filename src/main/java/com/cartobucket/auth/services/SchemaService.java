package com.cartobucket.auth.services;

import com.cartobucket.auth.model.generated.TemplateRequest;
import com.cartobucket.auth.model.generated.TemplateResponse;
import com.cartobucket.auth.model.generated.TemplateTypeEnum;
import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.cartobucket.auth.models.Template;
import com.networknt.schema.ValidationMessage;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SchemaService {
    Set<ValidationMessage> validateProfileAgainstSchema(Profile profile, Schema schema);
    TemplateResponse createSchema(Schema schemaRequest);

    void deleteSchema(UUID schemaId);

    Schema getSchema(UUID schemaId);

    List<Schema> getSchemasForAuthorizationServer(UUID authorizationServer);

    Schema updateSchema(UUID schemaId, Schema schemaRequest);


}
