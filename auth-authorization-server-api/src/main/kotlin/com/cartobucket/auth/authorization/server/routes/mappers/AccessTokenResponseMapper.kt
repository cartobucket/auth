// (C)2024
package com.cartobucket.auth.authorization.server.routes.mappers

import com.cartobucket.auth.authorization.server.dto.AccessTokenResponse
import com.cartobucket.auth.data.domain.AccessToken

object AccessTokenResponseMapper {
    fun AccessToken.toAccessTokenResponse(): AccessTokenResponse =
        AccessTokenResponse(
            accessToken = this.accessToken,
            expiresIn = this.expiresIn,
            tokenType =
                this.tokenType?.let {
                    AccessTokenResponse.TokenTypeEnum.fromValue(it.value())
                },
            scope = this.scope,
            refreshToken = this.refreshToken,
            idToken = this.idToken,
        )
}
