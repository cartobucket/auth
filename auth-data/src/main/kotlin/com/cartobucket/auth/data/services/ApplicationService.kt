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

package com.cartobucket.auth.data.services

import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Pair
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound
import com.cartobucket.auth.data.domain.Application
import com.cartobucket.auth.data.domain.ApplicationSecret
import com.cartobucket.auth.data.domain.Profile
import java.util.*

interface ApplicationService {

    @Throws(ApplicationNotFound::class)
    fun deleteApplication(applicationId: UUID)

    @Throws(ApplicationNotFound::class, ProfileNotFound::class)
    fun getApplication(applicationId: UUID): Pair<Application, Profile>

    fun createApplication(application: Application, profile: Profile): Pair<Application, Profile>

    @Throws(ApplicationNotFound::class)
    fun getApplicationSecrets(applicationId: List<UUID>): List<ApplicationSecret>

    @Throws(ApplicationNotFound::class)
    fun createApplicationSecret(applicationSecret: ApplicationSecret): ApplicationSecret

    @Throws(ApplicationSecretNoApplicationBadData::class, ApplicationSecretNotFound::class)
    fun deleteApplicationSecret(secretId: UUID)

    fun getApplications(authorizationServerIds: List<UUID>, page: Page): List<Application>

    fun isApplicationSecretValid(authorizationServerId: UUID, applicationSecretId: UUID, applicationSecret: String): Boolean
    
    fun getApplicationSecret(applicationSecretId: String): ApplicationSecret
}