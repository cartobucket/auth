package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class SchemaRequest {

    @NotNull
    private UUID authorizationServerId;
    private String name;
    private String schemaVersion;
    private Object schema;
    private Metadata metadata;

    public SchemaRequest() {}

    public SchemaRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public SchemaRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchemaRequest schemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
        return this;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public SchemaRequest schema(Object schema) {
        this.schema = schema;
        return this;
    }

    public Object getSchema() {
        return schema;
    }

    public void setSchema(Object schema) {
        this.schema = schema;
    }

    public SchemaRequest metadata(Metadata metadata) {
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
        SchemaRequest that = (SchemaRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(schemaVersion, that.schemaVersion) &&
                Objects.equals(schema, that.schema) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, name, schemaVersion, schema, metadata);
    }

    @Override
    public String toString() {
        return "SchemaRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", name='" + name + '\'' +
                ", schemaVersion='" + schemaVersion + '\'' +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }
}