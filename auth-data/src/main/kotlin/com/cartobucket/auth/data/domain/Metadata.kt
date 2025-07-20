package com.cartobucket.auth.data.domain

data class Metadata(
    var identifiers: List<Identifier>? = null,
    var schemaValidations: List<SchemaValidation>? = null,
    var properties: Map<String, Any>? = null
)