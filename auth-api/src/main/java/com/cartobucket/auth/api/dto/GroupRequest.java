package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class GroupRequest {

    private UUID authorizationServerId;
    private String displayName;
    private String externalId;
    private Metadata metadata;

    public GroupRequest() {}

    public GroupRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public GroupRequest displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public GroupRequest externalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public GroupRequest metadata(Metadata metadata) {
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
        GroupRequest that = (GroupRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(externalId, that.externalId) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, displayName, externalId, metadata);
    }

    @Override
    public String toString() {
        return "GroupRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", displayName='" + displayName + '\'' +
                ", externalId='" + externalId + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
