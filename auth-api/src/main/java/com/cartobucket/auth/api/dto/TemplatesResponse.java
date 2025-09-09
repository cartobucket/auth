package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class TemplatesResponse {
    
    @JsonbProperty("templates")
    private List<TemplateResponse> templates;
    
    @JsonbProperty("page")
    @NotNull
    private Page page;

    public TemplatesResponse() {}

    public TemplatesResponse templates(List<TemplateResponse> templates) {
        this.templates = templates;
        return this;
    }

    public List<TemplateResponse> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateResponse> templates) {
        this.templates = templates;
    }

    public TemplatesResponse page(Page page) {
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
        TemplatesResponse that = (TemplatesResponse) o;
        return Objects.equals(templates, that.templates) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templates, page);
    }

    @Override
    public String toString() {
        return "TemplatesResponse{" +
                "templates=" + templates +
                ", page=" + page +
                '}';
    }
}