package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class ApplicationsResponse {

    private List<ApplicationResponse> applications;
    @NotNull
    private Page page;

    public ApplicationsResponse() {}

    public ApplicationsResponse applications(List<ApplicationResponse> applications) {
        this.applications = applications;
        return this;
    }

    public List<ApplicationResponse> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationResponse> applications) {
        this.applications = applications;
    }

    public ApplicationsResponse page(Page page) {
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
        ApplicationsResponse that = (ApplicationsResponse) o;
        return Objects.equals(applications, that.applications) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applications, page);
    }

    @Override
    public String toString() {
        return "ApplicationsResponse{" +
                "applications=" + applications +
                ", page=" + page +
                '}';
    }
}