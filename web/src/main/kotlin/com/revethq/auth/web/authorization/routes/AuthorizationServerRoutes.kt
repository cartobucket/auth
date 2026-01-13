/*
 * Copyright 2023 Bryce Groff (Revet)
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

package com.revethq.auth.web.authorization.routes

import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.TemplateTypeEnum
import com.revethq.auth.core.exceptions.notfound.TemplateNotFound
import com.revethq.auth.core.services.ApplicationService
import com.revethq.auth.core.services.AuthorizationServerService
import com.revethq.auth.core.services.ClientService
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.core.services.TemplateService
import com.revethq.auth.core.services.UserService
import io.quarkus.qute.Qute
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import java.util.Base64
import java.util.UUID

@ApplicationScoped
open class AuthorizationServerRoutes : AuthorizationServer {

    private lateinit var templateServiceInstance: TemplateService

    @Suppress("unused")
    protected constructor() : super()

    @Inject
    constructor(
        authorizationServerService: AuthorizationServerService,
        clientService: ClientService,
        userService: UserService,
        templateServiceInstance: TemplateService,
        applicationService: ApplicationService,
        scopeService: ScopeService
    ) : super(
        authorizationServerService,
        clientService,
        userService,
        templateServiceInstance,
        applicationService,
        scopeService
    ) {
        this.templateServiceInstance = templateServiceInstance
    }

    override fun renderLoginScreen(authorizationServerId: UUID): Response {
        return renderLoginScreen(authorizationServerId, null)
    }

    override fun renderLoginScreen(authorizationServerId: UUID, errorMessage: String?): Response {
        val template = templateServiceInstance
            .getTemplates(listOf(authorizationServerId), Page(1, 0))
            .firstOrNull { it.templateType == TemplateTypeEnum.LOGIN }
            ?: throw TemplateNotFound()

        var quteTemplate = Qute.fmt(String(Base64.getDecoder().decode(template.template)))

        // Always set error data (null if no error)
        quteTemplate = quteTemplate.data("error", errorMessage)

        return Response
            .ok()
            .entity(quteTemplate.render())
            .build()
    }
}
