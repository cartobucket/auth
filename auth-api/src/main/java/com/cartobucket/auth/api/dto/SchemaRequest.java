package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class SchemaRequest {
    
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

    public SchemaRequest() {}

    public SchemaRequest authorizationServer(UUID authorizationServer) {
        this.authorizationServer = authorizationServer;
        return this;
    }

    public UUID getAuthorizationServer() {
        return authorizationServer;
    }

    public void setAuthorizationServer(UUID authorizationServer) {
        this.authorizationServer = authorizationServer;
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
        return Objects.equals(authorizationServer, that.authorizationServer) &&
                Objects.equals(name, that.name) &&
                Objects.equals(schemaVersion, that.schemaVersion) &&
                Objects.equals(schema, that.schema) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServer, name, schemaVersion, schema, metadata);
    }

    @Override
    public String toString() {
        return "SchemaRequest{" +
                "authorizationServer=" + authorizationServer +
                ", name='" + name + '\'' +
                ", schemaVersion='" + schemaVersion + '\'' +
                ", schema=" + schema +
                ", metadata=" + metadata +
                '}';
    }
}