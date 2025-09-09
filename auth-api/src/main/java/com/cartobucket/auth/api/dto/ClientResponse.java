package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class ClientResponse {
    
    @JsonbProperty("id")
    private String id;
    
    @JsonbProperty("name")
    private String name;
    
    @JsonbProperty("authorization_server_id")
    private UUID authorizationServerId;
    
    @JsonbProperty("redirect_uris")
    private List<String> redirectUris;
    
    @JsonbProperty("created_on")
    private OffsetDateTime createdOn;
    
    @JsonbProperty("updated_on")
    private OffsetDateTime updatedOn;
    
    @JsonbProperty("scopes")
    private List<ScopeResponse> scopes;
    
    @JsonbProperty("metadata")
    private Metadata metadata;

    public ClientResponse() {}

    public ClientResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientResponse name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public ClientResponse redirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
        return this;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public ClientResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ClientResponse updatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public ClientResponse scopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<ScopeResponse> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
    }

    public ClientResponse metadata(Metadata metadata) {
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
        ClientResponse that = (ClientResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(redirectUris, that.redirectUris) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(updatedOn, that.updatedOn) &&
                Objects.equals(scopes, that.scopes) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorizationServerId, redirectUris, createdOn, updatedOn, scopes, metadata);
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", redirectUris=" + redirectUris +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", scopes='" + scopes + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}