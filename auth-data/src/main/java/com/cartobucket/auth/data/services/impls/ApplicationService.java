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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.Application;
import com.cartobucket.auth.data.domain.ApplicationSecret;
import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.exceptions.badrequests.ApplicationSecretNoApplicationBadData;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ApplicationSecretNotFound;
import com.cartobucket.auth.data.exceptions.notfound.ProfileNotFound;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import org.graalvm.collections.Pair;

import java.util.List;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class ApplicationService implements com.cartobucket.auth.data.services.ApplicationService {

    @Override
    public void deleteApplication(UUID applicationId) throws ApplicationNotFound {

    }

    @Override
    public Pair<Application, Profile> getApplication(UUID applicationId) throws ApplicationNotFound, ProfileNotFound {
        return null;
    }

    @Override
    public Pair<Application, Profile> createApplication(Application application, Profile profile) {
        return null;
    }

    @Override
    public List<ApplicationSecret> getApplicationSecrets(UUID applicationId) throws ApplicationNotFound {
        return null;
    }

    @Override
    public ApplicationSecret createApplicationSecret(UUID applicationId, ApplicationSecret applicationSecret) throws ApplicationNotFound {
        return null;
    }

    @Override
    public void deleteApplicationSecret(UUID applicationId, UUID secretId) throws ApplicationSecretNoApplicationBadData, ApplicationSecretNotFound, ApplicationNotFound {

    }

    @Override
    public List<Application> getApplications(List<UUID> authorizationServerIds) {
        return null;
    }
}
