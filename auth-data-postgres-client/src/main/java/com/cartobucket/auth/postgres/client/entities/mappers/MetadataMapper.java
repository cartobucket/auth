package com.cartobucket.auth.postgres.client.entities.mappers;

import com.cartobucket.auth.data.domain.Identifier;
import com.cartobucket.auth.data.domain.Metadata;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.SchemaValidation;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class MetadataMapper {
    public static Metadata toMetadata(com.cartobucket.auth.rpc.Metadata metadata) {
        var _metadata = new Metadata();
        _metadata.setIdentifiers(metadata
                .getIdentifiersList()
                .stream()
                .map(MetadataMapper::fromProtoIdentifier)
                .toList()
        );
        _metadata.setProperties(Profile.fromProtoMap(metadata.getProperties().getFieldsMap()));
        _metadata.setSchemaValidations(metadata
                .getSchemaValidationsList()
                .stream()
                .map(MetadataMapper::fromProtoSchemaValidation)
                .toList()
        );
//        _metadata.setCreatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(metadata.getCreatedOn().getSeconds()), ZoneId.of("UTC")));
//        if (metadata.getUpdateDatesCount() > 0) {
//            _metadata.getUpdateDates().addAll(metadata
//                    .getUpdateDatesList()
//                    .stream()
//                    .map(updateDate -> OffsetDateTime.ofInstant(Instant.ofEpochSecond(updateDate.getSeconds()), ZoneId.of("UTC")))
//                    .toList());
//        }
        return _metadata;
    }

    private static SchemaValidation fromProtoSchemaValidation(com.cartobucket.auth.rpc.SchemaValidation schemaValidation) {
        var _schemaValidation = new SchemaValidation();
        _schemaValidation.setSchemaId(UUID.fromString(schemaValidation.getSchemaId()));
        _schemaValidation.setValid(schemaValidation.getIsValid());
        _schemaValidation.setValidatedOn(OffsetDateTime.ofInstant(Instant.ofEpochSecond(schemaValidation.getValidatedOn().getSeconds()), ZoneId.of("UTC")));
        return _schemaValidation;
    }

    public static Identifier fromProtoIdentifier(com.cartobucket.auth.rpc.Idenifier identifier) {
        var _identifier = new Identifier();
        _identifier.setSystem(identifier.getSystem());
        _identifier.setValue(identifier.getValue());
        return _identifier;
    }

    public static com.cartobucket.auth.rpc.Metadata from(Metadata metadata) {
        if (metadata == null) {
            return com.cartobucket.auth.rpc.Metadata.newBuilder().build();
        }
        var _metadata = com.cartobucket.auth.rpc.Metadata.newBuilder();
        _metadata.addAllIdentifiers(metadata
                .getIdentifiers()
                .stream()
                .map(MetadataMapper::toProtoIdentifier)
                .toList()
        );
        _metadata.setProperties(Profile.toProtoMap(metadata.getProperties()));
        _metadata.addAllSchemaValidations(metadata
                .getSchemaValidations()
                .stream()
                .map(MetadataMapper::toProtoSchemaValidation)
                .toList()
        );
        return _metadata.build();
    }

    private static com.cartobucket.auth.rpc.SchemaValidation toProtoSchemaValidation(SchemaValidation schemaValidation) {
        var _schemaValidation = com.cartobucket.auth.rpc.SchemaValidation.newBuilder();
        _schemaValidation.setSchemaId(schemaValidation.getSchemaId().toString());
        _schemaValidation.setIsValid(schemaValidation.isValid());
        _schemaValidation.setValidatedOn(Timestamp.newBuilder().setSeconds(schemaValidation.getValidatedOn().toEpochSecond()).build());
        return _schemaValidation.build();
    }

    private static com.cartobucket.auth.rpc.Idenifier toProtoIdentifier(Identifier identifier) {
        var _identifier = com.cartobucket.auth.rpc.Idenifier.newBuilder();
        _identifier.setSystem(identifier.getSystem());
        _identifier.setValue(identifier.getValue());
        return _identifier.build();
    }
}
