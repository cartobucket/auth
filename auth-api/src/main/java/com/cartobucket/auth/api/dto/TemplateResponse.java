package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class TemplateResponse {

    private String id;
    private UUID authorizationServerId;
    private String templateType;
    private String template;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;
    private Metadata metadata;

    public TemplateResponse() {}

    public TemplateResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TemplateResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public TemplateResponse templateType(String templateType) {
        this.templateType = templateType;
        return this;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public TemplateResponse template(String template) {
        this.template = template;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public TemplateResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public TemplateResponse updatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public TemplateResponse metadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateResponse that = (TemplateResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(templateType, that.templateType) &&
                Objects.equals(template, that.template) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(updatedOn, that.updatedOn) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServerId, templateType, template, createdOn, updatedOn, metadata);
    }

    @Override
    public String toString() {
        return "TemplateResponse{" +
                "id='" + id + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", templateType='" + templateType + '\'' +
                ", template='" + template + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", metadata=" + metadata +
                '}';
    }
}