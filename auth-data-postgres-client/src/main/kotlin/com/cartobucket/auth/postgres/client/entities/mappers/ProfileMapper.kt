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

package com.cartobucket.auth.postgres.client.entities.mappers

import com.cartobucket.auth.data.domain.Profile

object ProfileMapper {
    fun from(profile: com.cartobucket.auth.postgres.client.entities.Profile): Profile {
        val domainProfile = Profile()
        domainProfile.id = profile.id
        domainProfile.profile = profile.profile?.filterValues { it != null }?.mapValues { it.value!! }
        domainProfile.profileType = profile.profileType
        domainProfile.resource = profile.resource
        domainProfile.authorizationServerId = profile.authorizationServerId
        domainProfile.updatedOn = profile.updatedOn
        domainProfile.createdOn = profile.createdOn
        return domainProfile
    }

    fun to(profile: Profile): com.cartobucket.auth.postgres.client.entities.Profile {
        val entityProfile =
            com.cartobucket.auth.postgres.client.entities
                .Profile()
        entityProfile.id = profile.id
        entityProfile.profile = profile.profile
        entityProfile.profileType = profile.profileType
        entityProfile.resource = profile.resource
        entityProfile.authorizationServerId = profile.authorizationServerId
        entityProfile.updatedOn = profile.updatedOn
        entityProfile.createdOn = profile.createdOn
        return entityProfile
    }
}
