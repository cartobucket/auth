package com.cartobucket.auth.api.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import jakarta.json.bind.annotation.JsonbCreator;

/**
 * 
 */
public enum TemplateTypeEnum {
  
  LOGIN("login");

  private String value;

  TemplateTypeEnum(String value) {
    this.value = value;
  }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
    public static TemplateTypeEnum fromString(String s) {
      for (TemplateTypeEnum b : TemplateTypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonbCreator
  public static TemplateTypeEnum fromValue(String value) {
    for (TemplateTypeEnum b : TemplateTypeEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

        