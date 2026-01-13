package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

@JsonbNillable(false)
public class ApplicationSecretRequest {

    @NotNull
    private UUID applicationId;
    @NotNull
    private String name;
    @NotNull
    private List<UUID> scopes;
    private Integer expiresIn;

    public ApplicationSecretRequest() {}

    public ApplicationSecretRequest applicationId(UUID applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public ApplicationSecretRequest name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationSecretRequest scopes(List<UUID> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<UUID> getScopes() {
        return scopes;
    }

    public void setScopes(List<UUID> scopes) {
        this.scopes = scopes;
    }

    public ApplicationSecretRequest expiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationSecretRequest that = (ApplicationSecretRequest) o;
        return Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(scopes, that.scopes) &&
                Objects.equals(expiresIn, that.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, name, scopes, expiresIn);
    }

    @Override
    public String toString() {
        return "ApplicationSecretRequest{" +
                "applicationId=" + applicationId +
                ", name='" + name + '\'' +
                ", scopes=" + scopes +
                ", expiresIn=" + expiresIn +
                '}';
    }
}