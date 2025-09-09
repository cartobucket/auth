package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class AuthorizationServersResponse {
    
    @JsonbProperty("authorization_servers")
    private List<AuthorizationServerResponse> authorizationServers;
    
    @JsonbProperty("page")
    @NotNull
    private Page page;

    public AuthorizationServersResponse() {}

    public AuthorizationServersResponse authorizationServers(List<AuthorizationServerResponse> authorizationServers) {
        this.authorizationServers = authorizationServers;
        return this;
    }

    public List<AuthorizationServerResponse> getAuthorizationServers() {
        return authorizationServers;
    }

    public void setAuthorizationServers(List<AuthorizationServerResponse> authorizationServers) {
        this.authorizationServers = authorizationServers;
    }

    public AuthorizationServersResponse page(Page page) {
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
        AuthorizationServersResponse that = (AuthorizationServersResponse) o;
        return Objects.equals(authorizationServers, that.authorizationServers) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServers, page);
    }

    @Override
    public String toString() {
        return "AuthorizationServersResponse{" +
                "authorizationServers=" + authorizationServers +
                ", page=" + page +
                '}';
    }
}