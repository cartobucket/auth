/* (C)2024 */
package com.cartobucket.auth.authorization.server.dto

import jakarta.json.bind.annotation.JsonbNillable
import jakarta.json.bind.annotation.JsonbProperty
import jakarta.validation.constraints.NotNull

@JsonbNillable(false)
data class AuthorizationRequest(
    @field:JsonbProperty("client_id")
    @field:NotNull
    val clientId: String? = null,
    
    @field:JsonbProperty("response_type")
    @field:NotNull
    val responseType: String? = null,
    
    @field:JsonbProperty("redirect_uri")
    val redirectUri: String? = null,
    
    @field:JsonbProperty("scope")
    val scope: String? = null,
    
    @field:JsonbProperty("state")
    val state: String? = null,
    
    @field:JsonbProperty("nonce")
    val nonce: String? = null,
    
    @field:JsonbProperty("code_challenge")
    val codeChallenge: String? = null,
    
    @field:JsonbProperty("code_challenge_method")
    val codeChallengeMethod: String? = null,
    
    @field:JsonbProperty("username")
    val username: String? = null,
    
    @field:JsonbProperty("password")
    val password: String? = null
)