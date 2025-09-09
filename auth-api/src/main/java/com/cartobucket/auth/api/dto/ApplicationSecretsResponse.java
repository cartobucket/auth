package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class ApplicationSecretsResponse {
    
    @JsonbProperty("application_secrets")
    private List<ApplicationSecretResponse> applicationSecrets;
    
    @JsonbProperty("page")
    @NotNull
    private Page page;

    public ApplicationSecretsResponse() {}

    public ApplicationSecretsResponse applicationSecrets(List<ApplicationSecretResponse> applicationSecrets) {
        this.applicationSecrets = applicationSecrets;
        return this;
    }

    public List<ApplicationSecretResponse> getApplicationSecrets() {
        return applicationSecrets;
    }

    public void setApplicationSecrets(List<ApplicationSecretResponse> applicationSecrets) {
        this.applicationSecrets = applicationSecrets;
    }

    public ApplicationSecretsResponse page(Page page) {
        this.page = page;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationSecretsResponse that = (ApplicationSecretsResponse) o;
        return Objects.equals(applicationSecrets, that.applicationSecrets) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationSecrets, page);
    }

    @Override
    public String toString() {
        return "ApplicationSecretsResponse{" +
                "applicationSecrets=" + applicationSecrets +
                ", page=" + page +
                '}';
    }
}