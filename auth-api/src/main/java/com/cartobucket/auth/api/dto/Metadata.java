package com.cartobucket.auth.api.dto;

import com.cartobucket.auth.api.dto.MetadataIdentifiersInner;
import com.cartobucket.auth.api.dto.MetadataSchemaValidationsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import com.cartobucket.auth.api.server.validators.*;

/**
* 
**/
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-09-07T19:06:14.647087583-07:00[America/Los_Angeles]")
@jakarta.json.bind.annotation.JsonbNillable(false)

public class Metadata   {
    private  @Valid java.util.Map<String, Object> properties;
    private  @Valid List<@Valid MetadataIdentifiersInner> identifiers;
    private  @Valid List<@Valid MetadataSchemaValidationsInner> schemaValidations;

    /**
    **/
    public Metadata properties(java.util.Map<String, Object> properties) {
    this.properties = properties;
    return this;
    }

    
        @JsonbProperty("properties")
      public java.util.Map<String, Object> getProperties() {
    return properties;
    }

    @JsonbProperty("properties")
    public void setProperties(java.util.Map<String, Object> properties) {
    this.properties = properties;
    }

    /**
    **/
    public Metadata identifiers(List<@Valid MetadataIdentifiersInner> identifiers) {
    this.identifiers = identifiers;
    return this;
    }

    
        @JsonbProperty("identifiers")
      public List<MetadataIdentifiersInner> getIdentifiers() {
    return identifiers;
    }

    @JsonbProperty("identifiers")
    public void setIdentifiers(List<@Valid MetadataIdentifiersInner> identifiers) {
    this.identifiers = identifiers;
    }

        public Metadata addIdentifiersItem(MetadataIdentifiersInner identifiersItem) {
        if (this.identifiers == null) {
        this.identifiers = new ArrayList<>();
        }

        this.identifiers.add(identifiersItem);
        return this;
        }

        public Metadata removeIdentifiersItem(MetadataIdentifiersInner identifiersItem) {
        if (identifiersItem != null && this.identifiers != null) {
        this.identifiers.remove(identifiersItem);
        }

        return this;
        }
    /**
    **/
    public Metadata schemaValidations(List<@Valid MetadataSchemaValidationsInner> schemaValidations) {
    this.schemaValidations = schemaValidations;
    return this;
    }

    
        @JsonbProperty("schemaValidations")
      public List<MetadataSchemaValidationsInner> getSchemaValidations() {
    return schemaValidations;
    }

    @JsonbProperty("schemaValidations")
    public void setSchemaValidations(List<@Valid MetadataSchemaValidationsInner> schemaValidations) {
    this.schemaValidations = schemaValidations;
    }

        public Metadata addSchemaValidationsItem(MetadataSchemaValidationsInner schemaValidationsItem) {
        if (this.schemaValidations == null) {
        this.schemaValidations = new ArrayList<>();
        }

        this.schemaValidations.add(schemaValidationsItem);
        return this;
        }

        public Metadata removeSchemaValidationsItem(MetadataSchemaValidationsInner schemaValidationsItem) {
        if (schemaValidationsItem != null && this.schemaValidations != null) {
        this.schemaValidations.remove(schemaValidationsItem);
        }

        return this;
        }

@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    Metadata metadata = (Metadata) o;
    return Objects.equals(this.properties, metadata.properties) &&
    Objects.equals(this.identifiers, metadata.identifiers) &&
    Objects.equals(this.schemaValidations, metadata.schemaValidations);
}

@Override
public int hashCode() {
return Objects.hash(properties, identifiers, schemaValidations);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class Metadata {\n");

sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
sb.append("    identifiers: ").append(toIndentedString(identifiers)).append("\n");
sb.append("    schemaValidations: ").append(toIndentedString(schemaValidations)).append("\n");
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