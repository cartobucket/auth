package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class TemplateRequest {
    
    @JsonbProperty("authorization_server_id")
    private UUID authorizationServerId;
    
    @JsonbProperty("template_type")
    private String templateType;
    
    @JsonbProperty("template")
    private String template;
    
    @JsonbProperty("metadata")
    private Metadata metadata;

    public TemplateRequest() {}

    public TemplateRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public TemplateRequest templateType(String templateType) {
        this.templateType = templateType;
        return this;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public TemplateRequest template(String template) {
        this.template = template;
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public TemplateRequest metadata(Metadata metadata) {
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
        TemplateRequest that = (TemplateRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(templateType, that.templateType) &&
                Objects.equals(template, that.template) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, templateType, template, metadata);
    }

    @Override
    public String toString() {
        return "TemplateRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", templateType='" + templateType + '\'' +
                ", template='" + template + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}