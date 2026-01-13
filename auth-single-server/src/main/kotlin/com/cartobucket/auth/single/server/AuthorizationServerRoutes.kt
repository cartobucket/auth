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

package com.cartobucket.auth.single.server

import com.cartobucket.auth.authorization.server.routes.AuthorizationServer
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.TemplateTypeEnum
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound
import com.cartobucket.auth.data.services.ApplicationService
import com.cartobucket.auth.data.services.AuthorizationServerService
import com.cartobucket.auth.data.services.ClientService
import com.cartobucket.auth.data.services.ScopeService
import com.cartobucket.auth.data.services.TemplateService
import com.cartobucket.auth.data.services.UserService
import io.quarkus.qute.Qute
import jakarta.ws.rs.core.Response
import java.util.Base64
import java.util.UUID

class AuthorizationServerRoutes(
    authorizationServerService: AuthorizationServerService,
    clientService: ClientService,
    userService: UserService,
    private val templateServiceInstance: TemplateService,
    applicationService: ApplicationService,
    scopeService: ScopeService
) : AuthorizationServer(
    authorizationServerService,
    clientService,
    userService,
    templateServiceInstance,
    applicationService,
    scopeService
) {

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
