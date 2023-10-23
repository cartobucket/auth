package com.cartobucket.auth.data.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SchemaValidation {
    private UUID schemaId;
    private boolean isValid;
    private OffsetDateTime validatedOn;

    public UUID getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(UUID schemaId) {
        this.schemaId = schemaId;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public OffsetDateTime getValidatedOn() {
        return validatedOn;
    }

    public void setValidatedOn(OffsetDateTime validatedOn) {
        this.validatedOn = validatedOn;
    }
}
