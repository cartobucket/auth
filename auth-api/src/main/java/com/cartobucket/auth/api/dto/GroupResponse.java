package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class GroupResponse {

    private String id;
    private UUID authorizationServerId;
    private String displayName;
    private String externalId;
    private Metadata metadata;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;

    public GroupResponse() {}

    public GroupResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public GroupResponse displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public GroupResponse externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public GroupResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public GroupResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public GroupResponse updatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupResponse that = (GroupResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(externalId, that.externalId) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServerId, displayName, externalId, metadata, createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "GroupResponse{" +
                "id='" + id + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", displayName='" + displayName + '\'' +
                ", externalId='" + externalId + '\'' +
                ", metadata=" + metadata +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
