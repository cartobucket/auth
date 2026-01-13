package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class ScopeRequest {

    private UUID authorizationServerId;
    private String name;
    private Metadata metadata;

    public ScopeRequest() {}

    public ScopeRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public ScopeRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScopeRequest metadata(Metadata metadata) {
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
        ScopeRequest that = (ScopeRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, name, metadata);
    }

    @Override
    public String toString() {
        return "ScopeRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", name='" + name + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}