// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.ProfileType
import com.cartobucket.auth.data.domain.User

object ProfileMapper {
    fun toProfile(
        user: User,
        profile: Map<String, Any?>,
    ): Profile =
        Profile(
            profileType = ProfileType.User,
            profile = profile as? Map<String, Any>,
            resource = user.id,
            authorizationServerId = user.authorizationServerId,
        )

    fun toProfile(profile: Map<String, Any?>): Profile =
        Profile(
            profileType = ProfileType.Application,
            profile = profile as? Map<String, Any>,
        )
}
