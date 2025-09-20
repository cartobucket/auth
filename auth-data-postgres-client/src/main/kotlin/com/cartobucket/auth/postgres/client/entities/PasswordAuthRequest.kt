package com.cartobucket.auth.postgres.client.entities

class PasswordAuthRequest {
    var username: String? = null
    var password: String? = null
    var redirectUri: String? = null
    var state: String? = null
    var codeChallenge: String? = null
    var codeChallengeMethod: String? = null
    var nonce: String? = null
}
