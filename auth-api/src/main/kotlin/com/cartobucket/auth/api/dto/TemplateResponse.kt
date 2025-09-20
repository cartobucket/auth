// (C)2024
package com.cartobucket.auth.api.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import java.time.OffsetDateTime
import java.util.UUID

@JsonbNillable(false)
data class TemplateResponse(
    @field:JsonbProperty("id")
    val id: String? = null,
    @field:JsonbProperty("authorization_server_id")
    val authorizationServerId: UUID? = null,
    @field:JsonbProperty("template_type")
    val templateType: String? = null,
    @field:JsonbProperty("template")
    val template: String? = null,
    @field:JsonbProperty("created_on")
    val createdOn: OffsetDateTime? = null,
    @field:JsonbProperty("updated_on")
    val updatedOn: OffsetDateTime? = null,
    @field:JsonbProperty("metadata")
    val metadata: Metadata? = null,
)
