package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class ClientRequest {
    
    @JsonbProperty("name")
    private String name;
    
    @JsonbProperty("authorization_server_id")
    private UUID authorizationServerId;
    
    @JsonbProperty("redirect_uris")
    private List<String> redirectUris;
    
    @JsonbProperty("scopes")
    private List<UUID> scopes;
    
    @JsonbProperty("metadata")
    private Metadata metadata;

    public ClientRequest() {}

    public ClientRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public ClientRequest redirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
        return this;
    }

    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public ClientRequest scopes(List<UUID> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<UUID> getScopes() {
        return scopes;
    }

    public void setScopes(List<UUID> scopes) {
        this.scopes = scopes;
    }

    public ClientRequest metadata(Metadata metadata) {
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
        ClientRequest that = (ClientRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(redirectUris, that.redirectUris) &&
                Objects.equals(scopes, that.scopes) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, authorizationServerId, redirectUris, scopes, metadata);
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "name='" + name + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", redirectUris=" + redirectUris +
                ", scopes='" + scopes + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}