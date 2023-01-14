package com.cartobucket.auth.models;

import com.cartobucket.auth.model.generated.TemplateTypeEnum;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "authorizationServerId", "templateType" }) })
public class Template {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID authorizationServerId;

    private TemplateTypeEnum templateType;

    private byte[] template;

    private OffsetDateTime createdOn;

    private OffsetDateTime updatedOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public TemplateTypeEnum getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateTypeEnum templateType) {
        this.templateType = templateType;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
