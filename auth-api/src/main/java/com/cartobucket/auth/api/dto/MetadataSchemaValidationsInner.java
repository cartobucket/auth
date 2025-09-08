package com.cartobucket.auth.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import com.cartobucket.auth.api.server.validators.*;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-09-07T19:06:14.647087583-07:00[America/Los_Angeles]")
@jakarta.json.bind.annotation.JsonbNillable(false)

public class MetadataSchemaValidationsInner   {
    private  @Valid UUID schemaId;
    private  @Valid Boolean isValid;
    private  @Valid OffsetDateTime validatedOn;

    /**
    **/
    public MetadataSchemaValidationsInner schemaId(UUID schemaId) {
    this.schemaId = schemaId;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonbProperty("schemaId")
      public UUID getSchemaId() {
    return schemaId;
    }

    @JsonbProperty("schemaId")
    public void setSchemaId(UUID schemaId) {
    this.schemaId = schemaId;
    }

    /**
    **/
    public MetadataSchemaValidationsInner isValid(Boolean isValid) {
    this.isValid = isValid;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonbProperty("isValid")
      public Boolean getIsValid() {
    return isValid;
    }

    @JsonbProperty("isValid")
    public void setIsValid(Boolean isValid) {
    this.isValid = isValid;
    }

    /**
    **/
    public MetadataSchemaValidationsInner validatedOn(OffsetDateTime validatedOn) {
    this.validatedOn = validatedOn;
    return this;
    }

    
        @ApiModelProperty(value = "")
    @JsonbProperty("validatedOn")
      public OffsetDateTime getValidatedOn() {
    return validatedOn;
    }

    @JsonbProperty("validatedOn")
    public void setValidatedOn(OffsetDateTime validatedOn) {
    this.validatedOn = validatedOn;
    }


@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    MetadataSchemaValidationsInner metadataSchemaValidationsInner = (MetadataSchemaValidationsInner) o;
    return Objects.equals(this.schemaId, metadataSchemaValidationsInner.schemaId) &&
    Objects.equals(this.isValid, metadataSchemaValidationsInner.isValid) &&
    Objects.equals(this.validatedOn, metadataSchemaValidationsInner.validatedOn);
}

@Override
public int hashCode() {
return Objects.hash(schemaId, isValid, validatedOn);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class MetadataSchemaValidationsInner {\n");

sb.append("    schemaId: ").append(toIndentedString(schemaId)).append("\n");
sb.append("    isValid: ").append(toIndentedString(isValid)).append("\n");
sb.append("    validatedOn: ").append(toIndentedString(validatedOn)).append("\n");
sb.append("}");
return sb.toString();
}

/**
* Convert the given object to string with each line indented by 4 spaces
* (except the first line).
*/
private String toIndentedString(Object o) {
if (o == null) {
return "null";
}
return o.toString().replace("\n", "\n    ");
}


}