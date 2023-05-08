package com.cartobucket.auth.models;

import com.networknt.schema.SpecVersion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
public class Schema {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID authorizationServerId;
    private String name;
    private SpecVersion.VersionFlag jsonSchemaVersion;
    private String schema;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public SpecVersion.VersionFlag getJsonSchemaVersion() {
        return jsonSchemaVersion;
    }

    public void setJsonSchemaVersion(SpecVersion.VersionFlag jsonSchemaVersion) {
        this.jsonSchemaVersion = jsonSchemaVersion;
    }
}
