package com.cartobucket.auth.authorization.server.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "JSON Web Key Set")
@JsonbNillable(false)
public class JWKS {
    
    @JsonbProperty("keys")
    @Schema(description = "Array of JWK values", required = true)
    private List<JWK> keys;
    
    public List<JWK> getKeys() {
        return keys;
    }
    
    public void setKeys(List<JWK> keys) {
        this.keys = keys;
    }
}