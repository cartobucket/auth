package com.cartobucket.auth.api.dto;

import com.cartobucket.auth.api.dto.Metadata;
import java.time.OffsetDateTime;
import java.util.UUID;
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

public class ScopeResponse   {
    private  @Valid String id;
    private  @Valid UUID authorizationServerId;
    private  @Valid OffsetDateTime createdOn;
    private  @Valid OffsetDateTime updatedOn;
    private  @Valid String name;
    private  @Valid Metadata metadata;

    /**
    **/
    public ScopeResponse id(String id) {
    this.id = id;
    return this;
    }

    
        @JsonbProperty("id")
      public String getId() {
    return id;
    }

    @JsonbProperty("id")
    public void setId(String id) {
    this.id = id;
    }

    /**
        * 
    **/
    public ScopeResponse authorizationServerId(UUID authorizationServerId) {
    this.authorizationServerId = authorizationServerId;
    return this;
    }

    
        @JsonbProperty("authorizationServerId")
      public UUID getAuthorizationServerId() {
    return authorizationServerId;
    }

    @JsonbProperty("authorizationServerId")
    public void setAuthorizationServerId(UUID authorizationServerId) {
    this.authorizationServerId = authorizationServerId;
    }

    /**
    **/
    public ScopeResponse createdOn(OffsetDateTime createdOn) {
    this.createdOn = createdOn;
    return this;
    }

    
        @JsonbProperty("createdOn")
      public OffsetDateTime getCreatedOn() {
    return createdOn;
    }

    @JsonbProperty("createdOn")
    public void setCreatedOn(OffsetDateTime createdOn) {
    this.createdOn = createdOn;
    }

    /**
    **/
    public ScopeResponse updatedOn(OffsetDateTime updatedOn) {
    this.updatedOn = updatedOn;
    return this;
    }

    
        @JsonbProperty("updatedOn")
      public OffsetDateTime getUpdatedOn() {
    return updatedOn;
    }

    @JsonbProperty("updatedOn")
    public void setUpdatedOn(OffsetDateTime updatedOn) {
    this.updatedOn = updatedOn;
    }

    /**
    **/
    public ScopeResponse name(String name) {
    this.name = name;
    return this;
    }

    
        @JsonbProperty("name")
      public String getName() {
    return name;
    }

    @JsonbProperty("name")
    public void setName(String name) {
    this.name = name;
    }

    /**
    **/
    public ScopeResponse metadata(Metadata metadata) {
    this.metadata = metadata;
    return this;
    }

    
        @JsonbProperty("metadata")
      public Metadata getMetadata() {
    return metadata;
    }

    @JsonbProperty("metadata")
    public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
    }


@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    ScopeResponse scopeResponse = (ScopeResponse) o;
    return Objects.equals(this.id, scopeResponse.id) &&
    Objects.equals(this.authorizationServerId, scopeResponse.authorizationServerId) &&
    Objects.equals(this.createdOn, scopeResponse.createdOn) &&
    Objects.equals(this.updatedOn, scopeResponse.updatedOn) &&
    Objects.equals(this.name, scopeResponse.name) &&
    Objects.equals(this.metadata, scopeResponse.metadata);
}

@Override
public int hashCode() {
return Objects.hash(id, authorizationServerId, createdOn, updatedOn, name, metadata);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class ScopeResponse {\n");

sb.append("    id: ").append(toIndentedString(id)).append("\n");
sb.append("    authorizationServerId: ").append(toIndentedString(authorizationServerId)).append("\n");
sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
sb.append("    updatedOn: ").append(toIndentedString(updatedOn)).append("\n");
sb.append("    name: ").append(toIndentedString(name)).append("\n");
sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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