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

package com.revethq.auth.web.api.validators

import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.services.ApplicationService
import com.revethq.auth.core.services.AuthorizationServerService
import com.revethq.auth.core.api.dto.ApplicationRequest
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationRequest.Validator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@MustBeDocumented
annotation class ValidApplicationRequest(
    val message: String = "The Application was not valid",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
) {
    class Validator : ConstraintValidator<ValidApplicationRequest, ApplicationRequest> {
        @Inject
        lateinit var applicationService: ApplicationService

        @Inject
        lateinit var authorizationServerService: AuthorizationServerService

        override fun initialize(constraintAnnotation: ValidApplicationRequest) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(value: ApplicationRequest?, context: ConstraintValidatorContext): Boolean {
            return try {
                val authorizationServerId = value?.authorizationServerId ?: return false
                val authorizationServer = authorizationServerService.getAuthorizationServer(authorizationServerId)
                authorizationServer.scopes
                    ?.map { it.id }
                    ?.containsAll(value.scopes ?: emptyList()) ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}
