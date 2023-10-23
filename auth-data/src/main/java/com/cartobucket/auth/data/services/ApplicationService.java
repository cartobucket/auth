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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.Pair;
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    void deleteApplication(final UUID applicationId) throws ApplicationNotFound;

    Pair<Application, Profile> getApplication(final UUID applicationId) throws ApplicationNotFound, ProfileNotFound;

    Pair<Application, Profile> createApplication(final Application application, final Profile profile);

    List<ApplicationSecret> getApplicationSecrets(final List<UUID> applicationId) throws ApplicationNotFound;

    ApplicationSecret createApplicationSecret(final ApplicationSecret applicationSecret) throws ApplicationNotFound;

    void deleteApplicationSecret(final UUID secretId) throws ApplicationSecretNoApplicationBadData, ApplicationSecretNotFound;

    List<Application> getApplications(final List<UUID> authorizationServerIds);

    boolean isApplicationSecretValid(UUID authorizationServerId, UUID applicationId, String applicationSecret);
}
