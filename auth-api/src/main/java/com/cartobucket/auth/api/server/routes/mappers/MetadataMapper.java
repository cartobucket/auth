package com.cartobucket.auth.api.server.routes.mappers;

import com.cartobucket.auth.data.domain.Identifier;
import com.cartobucket.auth.data.domain.Metadata;
import com.cartobucket.auth.data.domain.SchemaValidation;
import com.cartobucket.auth.api.dto.MetadataIdentifiersInner;
import com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner;

import java.util.Collections;

public class MetadataMapper {

    public static Metadata from(com.cartobucket.auth.api.dto.Metadata metadata) {
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
            metadataDomain.setProperties(metadata.getProperties());
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

    public static com.cartobucket.auth.api.dto.Metadata to(Metadata metadata) {
        var _metadata = new com.cartobucket.auth.api.dto.Metadata();
        if (metadata == null) {
            _metadata.setIdentifiers(Collections.emptyList());
            _metadata.setProperties(Collections.emptyMap());
            _metadata.setSchemaValidations(Collections.emptyList());
            return _metadata;
        }
        if (metadata.getIdentifiers() == null) {
            _metadata.setIdentifiers(Collections.emptyList());
        } else {
            _metadata.setIdentifiers(metadata
                    .getIdentifiers()
                    .stream()
                    .map(MetadataMapper::toIdentifier)
                    .toList()
            );
        }
        if (metadata.getProperties() == null) {
            _metadata.setProperties(Collections.emptyMap());
        } else {
            _metadata.setProperties(metadata.getProperties());
        }
        if (metadata.getSchemaValidations() == null) {
            _metadata.setSchemaValidations(Collections.emptyList());
        } else {
            _metadata.setSchemaValidations(metadata
                    .getSchemaValidations()
                    .stream()
                    .map(MetadataMapper::toSchemaValidation)
                    .toList()
            );
        }
        return _metadata;
    }

    private static com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner toSchemaValidation(SchemaValidation schemaValidation) {
        var _schemaValidation = new com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner();
        _schemaValidation.setSchemaId(schemaValidation.getSchemaId());
        _schemaValidation.setIsValid(schemaValidation.isValid());
        _schemaValidation.setValidatedOn(schemaValidation.getValidatedOn());
        return _schemaValidation;
    }

    private static com.cartobucket.auth.api.dto.MetadataIdentifiersInner toIdentifier(Identifier identifier) {
        var _identifier = new com.cartobucket.auth.api.dto.MetadataIdentifiersInner();
        _identifier.setSystem(identifier.getSystem());
        _identifier.setValue(identifier.getValue());
        return _identifier;
    }
}
