package com.cartobucket.auth.data.domain

import java.time.OffsetDateTime
import java.util.UUID

data class SchemaValidation(
    var schemaId: UUID? = null,
    var isValid: Boolean = false,
    var validatedOn: OffsetDateTime? = null,
)
