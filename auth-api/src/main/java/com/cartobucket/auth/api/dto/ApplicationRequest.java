package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

@JsonbNillable(false)
public class ApplicationRequest {
    
    @JsonbProperty("authorization_server_id")
    @NotNull
    private UUID authorizationServerId;
    
    @JsonbProperty("name")
    @NotNull
    private String name;
    
    @JsonbProperty("client_id")
    private String clientId;
    
    @JsonbProperty("profile")
    private Object profile;
    
    @JsonbProperty("metadata")
    private Metadata metadata;
    
    @JsonbProperty("scopes")
    private List<UUID> scopes;

    public ApplicationRequest() {}

    public ApplicationRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public ApplicationRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationRequest clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ApplicationRequest profile(Object profile) {
        this.profile = profile;
        return this;
    }

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public ApplicationRequest metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ApplicationRequest scopes(List<UUID> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<UUID> getScopes() {
        return scopes;
    }

    public void setScopes(List<UUID> scopes) {
        this.scopes = scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationRequest that = (ApplicationRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(profile, that.profile) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(scopes, that.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, name, clientId, profile, metadata, scopes);
    }

    @Override
    public String toString() {
        return "ApplicationRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", name='" + name + '\'' +
                ", clientId='" + clientId + '\'' +
                ", profile=" + profile +
                ", metadata=" + metadata +
                ", scopes=" + scopes +
                '}';
    }
}