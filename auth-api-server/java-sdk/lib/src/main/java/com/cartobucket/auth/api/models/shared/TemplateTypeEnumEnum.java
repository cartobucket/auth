/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.shared;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TemplateTypeEnumEnum {
    LOGIN("login");

    @JsonValue
    public final String value;

    private TemplateTypeEnumEnum(String value) {
        this.value = value;
    }
}
