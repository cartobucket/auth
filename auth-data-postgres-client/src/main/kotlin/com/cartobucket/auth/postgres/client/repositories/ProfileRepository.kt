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

package com.cartobucket.auth.postgres.client.repositories

import com.revethq.iam.user.domain.ProfileType
import com.cartobucket.auth.postgres.client.entities.Profile
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.Optional
import java.util.UUID

@ApplicationScoped
class ProfileRepository : PanacheRepositoryBase<Profile, UUID> {
    fun findByResourceAndProfileType(resource: UUID, profileType: ProfileType): Optional<Profile> {
        return find("resource = ?1 and profileType = ?2", resource, profileType).singleResultOptional()
    }

    fun findByResourceId(resourceId: UUID): Optional<Profile> {
        return find("resource = ?1", resourceId).singleResultOptional()
    }

    fun deleteByResourceAndProfileType(resource: UUID, profileType: ProfileType) {
        delete("resource = ?1 and profiletype = ?2", resource, profileType.ordinal)
    }
}
