package com.cartobucket.auth.data.services.impls.mappers;

import com.cartobucket.auth.data.domain.Identifier;
import com.cartobucket.auth.data.domain.Metadata;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.SchemaValidation;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class MetadataMapper {
    public static Metadata from(com.cartobucket.auth.rpc.Metadata metadata) {
        var metadataDomain = new Metadata();
        metadataDomain.setIdentifiers(metadata
                .getIdentifiersList()
                .stream()
                .map(MetadataMapper::fromIdentifier)
                .toList()
        );
        metadataDomain.setSchemaValidations(metadata
                .getSchemaValidationsList()
                .stream()
                .map(MetadataMapper::fromSchemaValidation)
                .toList()
        );
        metadataDomain.setProperties(
                Profile.fromProtoMap(metadata.getProperties().getFieldsMap())
        );
        return metadataDomain;
    }

    private static SchemaValidation fromSchemaValidation(com.cartobucket.auth.rpc.SchemaValidation schemaValidation) {
        var _schemaValidation = new SchemaValidation();
        _schemaValidation.setSchemaId(UUID.fromString(schemaValidation.getSchemaId()));
        _schemaValidation.setValid(schemaValidation.getIsValid());
        _schemaValidation.setValidatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(schemaValidation.getValidatedOn().getSeconds()) , ZoneId.of("UTC")));
        return _schemaValidation;
    }

    private static Identifier fromIdentifier(com.cartobucket.auth.rpc.Idenifier idenifier) {
        var _identifier = new Identifier();
        _identifier.setSystem(idenifier.getSystem());
        _identifier.setValue(idenifier.getValue());
        return _identifier;
    }

    public static com.cartobucket.auth.rpc.Metadata to(Metadata metadata) {
        var _metadata = com.cartobucket.auth.rpc.Metadata.newBuilder();
        _metadata.addAllIdentifiers(metadata
                .getIdentifiers()
                .stream()
                .map(MetadataMapper::toIdentifier)
                .toList()
        );
        _metadata.setProperties(Profile.toProtoMap(metadata.getProperties()));
        return _metadata.build();
    }

    private static com.cartobucket.auth.rpc.Idenifier toIdentifier(Identifier identifier) {
        var _identifier = com.cartobucket.auth.rpc.Idenifier.newBuilder();
        _identifier.setSystem(identifier.getSystem());
        _identifier.setValue(identifier.getValue());
        return _identifier.build();
    }
}
