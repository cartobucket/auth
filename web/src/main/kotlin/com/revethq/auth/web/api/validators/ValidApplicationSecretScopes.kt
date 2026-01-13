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

import com.revethq.auth.core.domain.Page
import com.revethq.auth.core.domain.Scope
import com.revethq.auth.core.services.ApplicationService
import com.revethq.auth.core.services.ScopeService
import com.revethq.auth.core.api.dto.ApplicationSecretRequest
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationSecretScopes.Validator::class])
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
annotation class ValidApplicationSecretScopes(
    val message: String = "scopes must exist on the Authorization Server",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
) {
    class Validator : ConstraintValidator<ValidApplicationSecretScopes, ApplicationSecretRequest> {
        @Inject
        lateinit var scopeService: ScopeService

        @Inject
        lateinit var applicationService: ApplicationService

        override fun initialize(constraintAnnotation: ValidApplicationSecretScopes) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(value: ApplicationSecretRequest?, context: ConstraintValidatorContext): Boolean {
            val applicationId = value?.applicationId ?: return false
            val application = applicationService.getApplication(applicationId)
            val authorizationServerId = application.left?.authorizationServerId ?: return false
            val scopes = scopeService
                .getScopes(
                    listOf(authorizationServerId),
                    // TODO: This should probably get moved into the service, at the very least, this needs
                    //  to be iterated over until no scopes are left.
                    Page(100, 0)
                )
                .mapNotNull { it.id }
            return scopes.toHashSet().containsAll(value.scopes?.filterNotNull() ?: emptyList())
        }
    }
}
