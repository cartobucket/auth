package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class AuthorizationServerResponse {

    private String id;
    private String serverUrl;
    private String audience;
    private Integer clientCredentialsTokenExpiration;
    private Integer authorizationCodeTokenExpiration;
    @NotNull
    private String name;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private Metadata metadata;
    private List<ScopeResponse> scopes;

    public AuthorizationServerResponse() {}

    public AuthorizationServerResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthorizationServerResponse serverUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthorizationServerResponse audience(String audience) {
        this.audience = audience;
        return this;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public AuthorizationServerResponse clientCredentialsTokenExpiration(Integer clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
        return this;
    }

    public Integer getClientCredentialsTokenExpiration() {
        return clientCredentialsTokenExpiration;
    }

    public void setClientCredentialsTokenExpiration(Integer clientCredentialsTokenExpiration) {
        this.clientCredentialsTokenExpiration = clientCredentialsTokenExpiration;
    }

    public AuthorizationServerResponse authorizationCodeTokenExpiration(Integer authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
        return this;
    }

    public Integer getAuthorizationCodeTokenExpiration() {
        return authorizationCodeTokenExpiration;
    }

    public void setAuthorizationCodeTokenExpiration(Integer authorizationCodeTokenExpiration) {
        this.authorizationCodeTokenExpiration = authorizationCodeTokenExpiration;
    }

    public AuthorizationServerResponse name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorizationServerResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public AuthorizationServerResponse updatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public AuthorizationServerResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public AuthorizationServerResponse scopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<ScopeResponse> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationServerResponse that = (AuthorizationServerResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(audience, that.audience) &&
                Objects.equals(clientCredentialsTokenExpiration, that.clientCredentialsTokenExpiration) &&
                Objects.equals(authorizationCodeTokenExpiration, that.authorizationCodeTokenExpiration) &&
                Objects.equals(name, that.name) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(updatedOn, that.updatedOn) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(scopes, that.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverUrl, audience, clientCredentialsTokenExpiration, 
                          authorizationCodeTokenExpiration, name, createdOn, updatedOn, metadata, scopes);
    }

    @Override
    public String toString() {
        return "AuthorizationServerResponse{" +
                "id='" + id + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", audience='" + audience + '\'' +
                ", clientCredentialsTokenExpiration=" + clientCredentialsTokenExpiration +
                ", authorizationCodeTokenExpiration=" + authorizationCodeTokenExpiration +
                ", name='" + name + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", metadata=" + metadata +
                ", scopes=" + scopes +
                '}';
    }
}