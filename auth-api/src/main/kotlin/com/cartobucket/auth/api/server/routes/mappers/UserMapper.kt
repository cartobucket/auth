/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.api.server.routes.mappers

import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.domain.Profile
import com.cartobucket.auth.data.domain.User
import com.revethq.iam.user.domain.ProfileType
import com.cartobucket.auth.api.dto.UserRequest
import com.cartobucket.auth.api.dto.UserResponse

object UserMapper {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
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

    @JvmStatic
    fun toResponse(userProfilePair: Pair<User, Profile>): UserResponse {
        val user = userProfilePair.left!!
        val profile = userProfilePair.right
        return UserResponse().apply {
            id = user.id.toString()
            authorizationServerId = user.authorizationServerId
            username = user.username
            email = user.email
            metadata = MetadataMapper.to(user.metadata)
            createdOn = user.createdOn
            updatedOn = user.updatedOn
            this.profile = profile?.profile ?: emptyMap<String, Any>()
        }
    }
}
