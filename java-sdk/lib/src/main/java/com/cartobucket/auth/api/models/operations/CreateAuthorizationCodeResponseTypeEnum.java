/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CreateAuthorizationCodeResponseTypeEnum
 */
public enum CreateAuthorizationCodeResponseTypeEnum {
    CODE("code"),
    CLIENT_CREDENTIALS("client_credentials");

    @JsonValue
    public final String value;

    private CreateAuthorizationCodeResponseTypeEnum(String value) {
        this.value = value;
    }
}
