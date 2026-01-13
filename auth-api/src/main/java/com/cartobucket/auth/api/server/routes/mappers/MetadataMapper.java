package com.cartobucket.auth.api.server.routes.mappers;

import com.revethq.core.Identifier;
import com.revethq.core.Metadata;
import com.revethq.core.SchemaValidation;
import com.cartobucket.auth.api.dto.MetadataIdentifiersInner;
import com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetadataMapper {

    public static Metadata from(com.cartobucket.auth.api.dto.Metadata metadata) {
        if (metadata == null) {
            return new Metadata(Collections.emptyList(), Collections.emptyList(), Collections.emptyMap());
        }

        List<Identifier> identifiers = metadata.getIdentifiers() == null
                ? Collections.emptyList()
                : metadata.getIdentifiers().stream().map(MetadataMapper::fromIdentifier).toList();

        List<SchemaValidation> schemaValidations = metadata.getSchemaValidations() == null
                ? Collections.emptyList()
                : metadata.getSchemaValidations().stream().map(MetadataMapper::fromSchemaValidation).toList();

        Map<String, Object> properties = metadata.getProperties() == null
                ? Collections.emptyMap()
                : metadata.getProperties();

        return new Metadata(identifiers, schemaValidations, properties);
    }

    private static SchemaValidation fromSchemaValidation(MetadataSchemaValidationsInner metadataSchemaValidationsInner) {
        return new SchemaValidation(
                metadataSchemaValidationsInner.getSchemaId(),
                metadataSchemaValidationsInner.getIsValid() != null ? metadataSchemaValidationsInner.getIsValid() : false,
                metadataSchemaValidationsInner.getValidatedOn()
        );
    }

    private static Identifier fromIdentifier(MetadataIdentifiersInner metadataIdentifiersInner) {
        return new Identifier(
                metadataIdentifiersInner.getSystem(),
                metadataIdentifiersInner.getValue()
        );
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
