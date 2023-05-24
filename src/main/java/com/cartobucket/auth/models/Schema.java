package com.cartobucket.auth.models;

import com.networknt.schema.SpecVersion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
public class Schema {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID authorizationServerId;
    private String name;
    private SpecVersion.VersionFlag jsonSchemaVersion;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> schema;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    public Map<String, Object> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Object> schema) {
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
