/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * InitiateAuthorizationResponseTypeEnum
 */
public enum InitiateAuthorizationResponseTypeEnum {
    CODE("code"),
    CLIENT_CREDENTIALS("client_credentials");

    @JsonValue
    public final String value;

    private InitiateAuthorizationResponseTypeEnum(String value) {
        this.value = value;
    }
}
