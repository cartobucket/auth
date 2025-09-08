package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class SchemaResponse {
    
    @JsonbProperty("id")
    private UUID id;
    
    @JsonbProperty("authorization_server")
    @NotNull
    private UUID authorizationServer;
    
    @JsonbProperty("name")
    private String name;
    
    @JsonbProperty("schema_version")
    private String schemaVersion;
    
    @JsonbProperty("schema")
    private Object schema;
    
    @JsonbProperty("metadata")
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

    public SchemaResponse authorizationServer(UUID authorizationServer) {
        this.authorizationServer = authorizationServer;
        return this;
    }

    public UUID getAuthorizationServer() {
        return authorizationServer;
    }

    public void setAuthorizationServer(UUID authorizationServer) {
        this.authorizationServer = authorizationServer;
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
                Objects.equals(authorizationServer, that.authorizationServer) &&
                Objects.equals(name, that.name) &&
                Objects.equals(schemaVersion, that.schemaVersion) &&
                Objects.equals(schema, that.schema) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServer, name, schemaVersion, schema, metadata);
    }

    @Override
    public String toString() {
        return "SchemaResponse{" +
                "id=" + id +
                ", authorizationServer=" + authorizationServer +
                ", name='" + name + '\'' +
                ", schemaVersion='" + schemaVersion + '\'' +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }
}