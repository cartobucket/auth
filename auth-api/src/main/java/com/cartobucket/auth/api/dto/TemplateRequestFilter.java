package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class TemplateRequestFilter {
    
    @JsonbProperty("authorization_server_ids")
    private List<UUID> authorizationServerIds;

    public TemplateRequestFilter() {}

    public TemplateRequestFilter authorizationServerIds(List<UUID> authorizationServerIds) {
        this.authorizationServerIds = authorizationServerIds;
        return this;
    }

    public List<UUID> getAuthorizationServerIds() {
        return authorizationServerIds;
    }

    public void setAuthorizationServerIds(List<UUID> authorizationServerIds) {
        this.authorizationServerIds = authorizationServerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateRequestFilter that = (TemplateRequestFilter) o;
        return Objects.equals(authorizationServerIds, that.authorizationServerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerIds);
    }

    @Override
    public String toString() {
        return "TemplateRequestFilter{" +
                "authorizationServerIds=" + authorizationServerIds +
                '}';
    }
}