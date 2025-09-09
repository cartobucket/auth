package com.cartobucket.auth.api.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import com.cartobucket.auth.api.server.validators.*;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-09-07T19:06:14.647087583-07:00[America/Los_Angeles]")
@jakarta.json.bind.annotation.JsonbNillable(false)

public class MetadataIdentifiersInner   {
    private  @Valid String system;
    private  @Valid String value;

    /**
    **/
    public MetadataIdentifiersInner system(String system) {
    this.system = system;
    return this;
    }

    
        @JsonbProperty("system")
      public String getSystem() {
    return system;
    }

    @JsonbProperty("system")
    public void setSystem(String system) {
    this.system = system;
    }

    /**
    **/
    public MetadataIdentifiersInner value(String value) {
    this.value = value;
    return this;
    }

    
        @JsonbProperty("value")
      public String getValue() {
    return value;
    }

    @JsonbProperty("value")
    public void setValue(String value) {
    this.value = value;
    }


@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    MetadataIdentifiersInner metadataIdentifiersInner = (MetadataIdentifiersInner) o;
    return Objects.equals(this.system, metadataIdentifiersInner.system) &&
    Objects.equals(this.value, metadataIdentifiersInner.value);
}

@Override
public int hashCode() {
return Objects.hash(system, value);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class MetadataIdentifiersInner {\n");

sb.append("    system: ").append(toIndentedString(system)).append("\n");
sb.append("    value: ").append(toIndentedString(value)).append("\n");
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