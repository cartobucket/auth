// (C)2024
package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.api.dto.UserRequest
import com.cartobucket.auth.api.dto.UserResponse
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.ProfileType
import com.cartobucket.auth.data.domain.User

object UserMapper {
    fun from(userRequest: UserRequest): Pair<User, Profile> {
        val user = User()
        user.authorizationServerId = userRequest.authorizationServerId
        user.metadata = MetadataMapper.from(userRequest.metadata)
        user.username = userRequest.username
        user.email = userRequest.email
        user.password = userRequest.password

        val profile = Profile()
        profile.profileType = ProfileType.User
        profile.profile = userRequest.profile as? Map<String, Any>
        profile.authorizationServerId = userRequest.authorizationServerId
        return Pair.create(user, profile)
    }

    fun toResponse(userProfilePair: Pair<User, Profile>): UserResponse {
        val user = userProfilePair.left!!
        val profile = userProfilePair.right!!
        return UserResponse(
            id = user.id.toString(),
            authorizationServerId = user.authorizationServerId,
            username = user.username,
            email = user.email,
            metadata = MetadataMapper.to(user.metadata),
            createdOn = user.createdOn,
            updatedOn = user.updatedOn,
            profile = profile.profile ?: emptyMap<String, Any?>(),
        )
    }
}
