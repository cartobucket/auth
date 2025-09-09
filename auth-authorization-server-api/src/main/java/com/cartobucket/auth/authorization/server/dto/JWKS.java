package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.List;

@JsonbNillable(false)
public class JWKS {
    
    @JsonbProperty("keys")
    private List<JWK> keys;
    
    public List<JWK> getKeys() {
        return keys;
    }
    
    public void setKeys(List<JWK> keys) {
        this.keys = keys;
    }
}