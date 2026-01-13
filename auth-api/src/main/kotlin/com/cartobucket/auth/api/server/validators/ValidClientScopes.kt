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

package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.services.ScopeService
import com.cartobucket.auth.api.dto.ClientRequest
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidClientScopes.Validator::class])
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
annotation class ValidClientScopes(
    val message: String = "scopes must exist on the Authorization Server",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
) {
    class Validator : ConstraintValidator<ValidClientScopes, ClientRequest> {
        @Inject
        lateinit var scopeService: ScopeService

        override fun initialize(constraintAnnotation: ValidClientScopes) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(value: ClientRequest?, context: ConstraintValidatorContext): Boolean {
            if (value?.authorizationServerId == null || value.scopes == null) {
                return false
            }

            val scopes = scopeService
                .getScopes(
                    listOf(value.authorizationServerId!!),
                    // TODO: This should probably get moved into the service, at the very least, this needs
                    //  to be iterated over until no scopes are left.
                    Page(100, 0)
                )
                .map { it.id }
            return scopes.toHashSet().containsAll(value.scopes!!)
        }
    }
}
