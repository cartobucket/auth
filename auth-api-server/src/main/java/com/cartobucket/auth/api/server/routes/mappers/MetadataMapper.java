package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.Identifier;
import com.cartobucket.auth.data.domain.Metadata;
import com.cartobucket.auth.data.domain.SchemaValidation;
import com.cartobucket.auth.model.generated.MetadataIdentifiersInner;
import com.cartobucket.auth.model.generated.MetadataSchemaValidationsInner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

public class MetadataMapper {

    public static Metadata from(com.cartobucket.auth.model.generated.Metadata metadata) {
        var metadataDomain = new Metadata();
        if (metadata == null) {
            metadataDomain.setIdentifiers(Collections.emptyList());
            metadataDomain.setSchemaValidations(Collections.emptyList());
            metadataDomain.setProperties(Collections.emptyMap());
            return metadataDomain;
        }
        if (metadata.getIdentifiers() == null) {
            metadataDomain.setIdentifiers(Collections.emptyList());
        } else {
            metadataDomain.setIdentifiers(metadata
                    .getIdentifiers()
                    .stream()
                    .map(MetadataMapper::fromIdentifier)
                    .toList()
            );
        }
        if (metadata.getSchemaValidations() == null) {
            metadataDomain.setSchemaValidations(Collections.emptyList());
        } else {
            metadataDomain.setSchemaValidations(metadata
                    .getSchemaValidations()
                    .stream()
                    .map(MetadataMapper::fromSchemaValidation)
                    .toList()
            );
        }
        if (metadata.getProperties() == null) {
            metadataDomain.setProperties(Collections.emptyMap());
        } else {
            metadataDomain.setProperties(
                    new ObjectMapper().convertValue(metadata.getProperties(),
                            new TypeReference<>() {
                            }
                    )
            );
        }
        return metadataDomain;
    }

    private static SchemaValidation fromSchemaValidation(MetadataSchemaValidationsInner metadataSchemaValidationsInner) {
        var schemaValidation = new SchemaValidation();
        schemaValidation.setSchemaId(metadataSchemaValidationsInner.getSchemaId());
        schemaValidation.setValid(metadataSchemaValidationsInner.getIsValid());
        schemaValidation.setValidatedOn(metadataSchemaValidationsInner.getValidatedOn());
        return schemaValidation;
    }

    private static Identifier fromIdentifier(MetadataIdentifiersInner metadataIdentifiersInner) {
        var identifier = new Identifier();
        identifier.setSystem(metadataIdentifiersInner.getSystem());
        identifier.setValue(metadataIdentifiersInner.getValue());
        return identifier;
    }

    public static com.cartobucket.auth.model.generated.Metadata to(Metadata metadata) {
        var _metadata = new com.cartobucket.auth.model.generated.Metadata();
        _metadata.setIdentifiers(metadata
                .getIdentifiers()
                .stream()
                .map(MetadataMapper::toIdentifier)
                .toList()
        );
        _metadata.setProperties(metadata.getProperties());
        _metadata.setSchemaValidations(metadata
                .getSchemaValidations()
                .stream()
                .map(MetadataMapper::toSchemaValidation)
                .toList()
        );
        return _metadata;
    }

    private static com.cartobucket.auth.model.generated.MetadataSchemaValidationsInner toSchemaValidation(SchemaValidation schemaValidation) {
        var _schemaValidation = new com.cartobucket.auth.model.generated.MetadataSchemaValidationsInner();
        _schemaValidation.setSchemaId(schemaValidation.getSchemaId());
        _schemaValidation.setIsValid(schemaValidation.isValid());
        _schemaValidation.setValidatedOn(schemaValidation.getValidatedOn());
        return _schemaValidation;
    }

    private static com.cartobucket.auth.model.generated.MetadataIdentifiersInner toIdentifier(Identifier identifier) {
        var _identifier = new com.cartobucket.auth.model.generated.MetadataIdentifiersInner();
        _identifier.setSystem(identifier.getSystem());
        _identifier.setValue(identifier.getValue());
        return _identifier;
    }
}
