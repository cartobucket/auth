package com.cartobucket.auth.data.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class Metadata {
    private List<Identifier> identifiers;
    private List<SchemaValidation> schemaValidations;
    private Map<String, Object> properties;
    private OffsetDateTime createdOn;
    private List<OffsetDateTime> updateDates;

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public List<SchemaValidation> getSchemaValidations() {
        return schemaValidations;
    }

    public void setSchemaValidations(List<SchemaValidation> schemaValidations) {
        this.schemaValidations = schemaValidations;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<OffsetDateTime> getUpdateDates() {
        return updateDates;
    }

    public void setUpdateDates(List<OffsetDateTime> updateDates) {
        this.updateDates = updateDates;
    }
}
