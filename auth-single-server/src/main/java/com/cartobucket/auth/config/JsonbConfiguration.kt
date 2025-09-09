package com.cartobucket.auth.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.json.bind.Jsonb
import jakarta.json.bind.JsonbBuilder
import jakarta.json.bind.JsonbConfig

@ApplicationScoped
class JsonbConfiguration {
    @Produces
    @ApplicationScoped
    fun createJsonb(): Jsonb? {
        val config = JsonbConfig()
        return JsonbBuilder.create(config)
    }
}
