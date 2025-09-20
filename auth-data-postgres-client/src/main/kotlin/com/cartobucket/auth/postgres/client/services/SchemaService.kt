package com.cartobucket.auth.postgres.client.services

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SchemaService {
    // TODO: Convert from Java implementation
    fun getSchemaByNameAndAuthorizationServerId(
        name: String,
        authorizationServerId: java.util.UUID,
    ): com.cartobucket.auth.data.domain.Schema? {
        // Placeholder implementation
        return null
    }

    fun validateProfileAgainstSchema(
        profile: com.cartobucket.auth.data.domain.Profile,
        schema: com.cartobucket.auth.data.domain.Schema,
    ): List<String> {
        // Placeholder implementation
        return emptyList()
    }
}
