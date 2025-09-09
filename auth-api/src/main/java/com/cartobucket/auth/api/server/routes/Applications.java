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

package com.cartobucket.auth.api.server.routes;

import com.cartobucket.auth.api.server.routes.mappers.ApplicationMapper;
import com.cartobucket.auth.api.server.routes.mappers.ProfileMapper;
import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.api.interfaces.ApplicationsApi;
import com.cartobucket.auth.api.dto.ApplicationRequest;
import com.cartobucket.auth.api.dto.ApplicationsResponse;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.cartobucket.auth.api.server.routes.Constants.LIMIT_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Constants.OFFSET_DEFAULT;
import static com.cartobucket.auth.api.server.routes.Pagination.getPage;

public class Applications implements ApplicationsApi {
    final ApplicationService applicationService;

    final AuthorizationServerService authorizationServerService;

    public Applications(ApplicationService applicationService, AuthorizationServerService authorizationServerService) {
        this.applicationService = applicationService;
        this.authorizationServerService = authorizationServerService;
    }

    @Override
    public Response createApplication(ApplicationRequest applicationRequest) {
        final var application = applicationService
                .createApplication(
                        ApplicationMapper.from(applicationRequest),
                        ProfileMapper.toProfile((Map<String, Object>) applicationRequest.getProfile())
                );
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toResponseWithProfile(
                                application.getLeft(),
                                application.getRight()
                        )
                )
                .build();
    }

    @Override
    public Response deleteApplication(UUID applicationId) {
        applicationService.deleteApplication(applicationId);
        return Response
                .ok()
                .build();
    }

    @Override
    public Response getApplication(UUID applicationId) {
        var application = applicationService.getApplication(applicationId);
        return Response
                .ok()
                .entity(
                        ApplicationMapper.toResponseWithProfile(
                            application.getLeft(),
                            application.getRight()
                    )
                )
                .build();
    }

    @Override
    public Response listApplications(List<UUID> authorizationServerIds, Integer limit, Integer offset) {
        // TODO: Probably makes senes to move to Kotlin and use default parameters
        if (limit == null) {
            limit = LIMIT_DEFAULT;
        }
        if (offset == null) {
            offset = OFFSET_DEFAULT;
        }

        final var applicationsResponse = new ApplicationsResponse();
        applicationsResponse.setApplications(
                applicationService
                        .getApplications(
                                authorizationServerIds,
                                new Page(limit, offset)
                        )
                        .stream()
                        .map(ApplicationMapper::toResponse)
                        .collect(Collectors.toList())
        );
        applicationsResponse.setPage(
                getPage("applications", authorizationServerIds, limit, offset)
        );
        return Response
                .ok()
                .entity(applicationsResponse)
                .build();
    }
}
