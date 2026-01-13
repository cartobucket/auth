package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class SchemaResponse {

    private UUID id;
    @NotNull
    private UUID authorizationServerId;
    private String name;
    private String schemaVersion;
    private Object schema;
    private Metadata metadata;

    public SchemaResponse() {}

    public SchemaResponse id(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SchemaResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public SchemaResponse name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchemaResponse schemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
        return this;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public SchemaResponse schema(Object schema) {
        this.schema = schema;
        return this;
    }

    public Object getSchema() {
        return schema;
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }

    public SchemaResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaResponse that = (SchemaResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(schemaVersion, that.schemaVersion) &&
                Objects.equals(schema, that.schema) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServerId, name, schemaVersion, schema, metadata);
    }

    @Override
    public String toString() {
        return "SchemaResponse{" +
                "id=" + id +
                ", authorizationServerId=" + authorizationServerId +
                ", name='" + name + '\'' +
                ", schemaVersion='" + schemaVersion + '\'' +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }
}