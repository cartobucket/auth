package com.cartobucket.auth.api.dto;

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

public class Page   {
    private  @Valid String next;
    private  @Valid String previous;

    /**
    **/
    public Page next(String next) {
    this.next = next;
    return this;
    }

    
        @JsonbProperty("next")
      public String getNext() {
    return next;
    }

    @JsonbProperty("next")
    public void setNext(String next) {
    this.next = next;
    }

    /**
    **/
    public Page previous(String previous) {
    this.previous = previous;
    return this;
    }

    
        @JsonbProperty("previous")
      public String getPrevious() {
    return previous;
    }

    @JsonbProperty("previous")
    public void setPrevious(String previous) {
    this.previous = previous;
    }


@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    Page page = (Page) o;
    return Objects.equals(this.next, page.next) &&
    Objects.equals(this.previous, page.previous);
}

@Override
public int hashCode() {
return Objects.hash(next, previous);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class Page {\n");

sb.append("    next: ").append(toIndentedString(next)).append("\n");
sb.append("    previous: ").append(toIndentedString(previous)).append("\n");
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