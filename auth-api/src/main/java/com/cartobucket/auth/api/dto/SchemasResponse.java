package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class SchemasResponse {

    private List<SchemaResponse> schemas;
    @NotNull
    private Page page;

    public SchemasResponse() {}

    public SchemasResponse schemas(List<SchemaResponse> schemas) {
        this.schemas = schemas;
        return this;
    }

    public List<SchemaResponse> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<SchemaResponse> schemas) {
        this.schemas = schemas;
    }

    public SchemasResponse page(Page page) {
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
        SchemasResponse that = (SchemasResponse) o;
        return Objects.equals(schemas, that.schemas) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemas, page);
    }

    @Override
    public String toString() {
        return "SchemasResponse{" +
                "schemas=" + schemas +
                ", page=" + page +
                '}';
    }
}