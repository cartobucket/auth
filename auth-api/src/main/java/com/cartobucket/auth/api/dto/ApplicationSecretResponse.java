package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

@JsonbNillable(false)
public class ApplicationSecretResponse {

    private UUID id;
    private String clientSecret;
    private String name;
    private Integer expiresIn;
    private UUID applicationId;
    private UUID authorizationServerId;
    @NotNull
    private List<ScopeResponse> scopes;
    @NotNull
    private OffsetDateTime createdOn;

    public ApplicationSecretResponse() {}

    public ApplicationSecretResponse id(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ApplicationSecretResponse clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public ApplicationSecretResponse name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationSecretResponse expiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public ApplicationSecretResponse applicationId(UUID applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationSecretResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public ApplicationSecretResponse scopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<ScopeResponse> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
    }

    public ApplicationSecretResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationSecretResponse that = (ApplicationSecretResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientSecret, that.clientSecret) &&
                Objects.equals(name, that.name) &&
                Objects.equals(expiresIn, that.expiresIn) &&
                Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(scopes, that.scopes) &&
                Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientSecret, name, expiresIn, applicationId, authorizationServerId, scopes, createdOn);
    }

    @Override
    public String toString() {
        return "ApplicationSecretResponse{" +
                "id=" + id +
                ", clientSecret='" + clientSecret + '\'' +
                ", name='" + name + '\'' +
                ", expiresIn=" + expiresIn +
                ", applicationId=" + applicationId +
                ", authorizationServerId=" + authorizationServerId +
                ", scopes=" + scopes +
                ", createdOn=" + createdOn +
                '}';
    }
}