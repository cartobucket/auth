package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class ScopesResponse {
    
    @JsonbProperty("scopes")
    private List<ScopeResponse> scopes;
    
    @JsonbProperty("page")
    @NotNull
    private Page page;

    public ScopesResponse() {}

    public ScopesResponse scopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
        return this;
    }

    public List<ScopeResponse> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeResponse> scopes) {
        this.scopes = scopes;
    }

    public ScopesResponse page(Page page) {
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
        ScopesResponse that = (ScopesResponse) o;
        return Objects.equals(scopes, that.scopes) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scopes, page);
    }

    @Override
    public String toString() {
        return "ScopesResponse{" +
                "scopes=" + scopes +
                ", page=" + page +
                '}';
    }
}