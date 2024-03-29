/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.cartobucket.auth.api.models.operations;

import com.cartobucket.auth.api.utils.SpeakeasyMetadata;

public class ListClientsRequest {
    @SpeakeasyMetadata("queryParam:style=form,explode=true,name=authorizationServerIds")
    public String[] authorizationServerIds;
    public ListClientsRequest withAuthorizationServerIds(String[] authorizationServerIds) {
        this.authorizationServerIds = authorizationServerIds;
        return this;
    }
    
    public ListClientsRequest(){}
}
