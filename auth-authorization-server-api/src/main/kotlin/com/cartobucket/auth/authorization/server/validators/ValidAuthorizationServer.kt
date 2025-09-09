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

package com.cartobucket.auth.authorization.server.validators

import com.cartobucket.auth.data.exceptions.notfound.AuthorizationServerNotFound
import com.cartobucket.auth.data.services.AuthorizationServerService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.UUID
import kotlin.reflect.KClass

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = [ValidAuthorizationServer.Validator::class])
@Target(
    java.lang.annotation.ElementType.METHOD,
    java.lang.annotation.ElementType.FIELD,
    java.lang.annotation.ElementType.ANNOTATION_TYPE,
    java.lang.annotation.ElementType.CONSTRUCTOR,
    java.lang.annotation.ElementType.PARAMETER,
    java.lang.annotation.ElementType.TYPE_USE,
)
@SupportedValidationTarget(ANNOTATED_ELEMENT)
@Documented
annotation class ValidAuthorizationServer(
    val message: String = "The Authorization Server was not found",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidAuthorizationServer, UUID> {
        @Inject
        lateinit var applicationService: AuthorizationServerService

        override fun initialize(constraintAnnotation: ValidAuthorizationServer) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: UUID?,
            context: ConstraintValidatorContext,
        ): Boolean {
            if (value == null) return true

            return try {
                applicationService.getAuthorizationServer(value)
                true
            } catch (e: AuthorizationServerNotFound) {
                context
                    .buildConstraintViolationWithTemplate("The Authorization Server was not found with the given ID of $value")
                    .addConstraintViolation()
                false
            } catch (e: Exception) {
                println("An unexpected error occurred while validating the Authorization Server ID of $value: ${e.message}")
                true
            }
        }
    }
}
