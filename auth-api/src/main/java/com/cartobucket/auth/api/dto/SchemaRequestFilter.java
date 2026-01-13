package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class SchemaRequestFilter {

    private List<UUID> authorizationServerIds;

    public SchemaRequestFilter() {}

    public SchemaRequestFilter authorizationServerIds(List<UUID> authorizationServerIds) {
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
        SchemaRequestFilter that = (SchemaRequestFilter) o;
        return Objects.equals(authorizationServerIds, that.authorizationServerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerIds);
    }

    @Override
    public String toString() {
        return "SchemaRequestFilter{" +
                "authorizationServerIds=" + authorizationServerIds +
                '}';
    }
}