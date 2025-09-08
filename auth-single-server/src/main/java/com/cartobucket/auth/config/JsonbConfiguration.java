package com.cartobucket.auth.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

@ApplicationScoped
public class JsonbConfiguration {
    
    @Produces
    @ApplicationScoped
    public Jsonb createJsonb() {
        JsonbConfig config = new JsonbConfig();
        return JsonbBuilder.create(config);
    }
}