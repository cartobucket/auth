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

import com.cartobucket.auth.authorization.server.dto.AccessTokenRequest
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidAccessTokenRequest.Validator::class])
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
annotation class ValidAccessTokenRequest(
    val message: String = "The Access Token request is not valid",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = []
) {
    class Validator : ConstraintValidator<ValidAccessTokenRequest, AccessTokenRequest> {
        override fun initialize(constraintAnnotation: ValidAccessTokenRequest) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(value: AccessTokenRequest?, context: ConstraintValidatorContext): Boolean {
            if (value == null) return false

            if (value.grantType == null) {
                context.buildConstraintViolationWithTemplate("The Access Token request must contain a grant_type field.")
                    .addPropertyNode("grant_type")
                    .addConstraintViolation()
                return false
            }

            return when (value.grantType) {
                AccessTokenRequest.GrantTypeEnum.CLIENT_CREDENTIALS -> {
                    if (value.clientId == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_id field when using the client_credentials grant_type.")
                            .addPropertyNode("client_id")
                            .addConstraintViolation()
                        return false
                    }
                    if (value.clientSecret == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_secret field when using the client_credentials grant_type.")
                            .addPropertyNode("client_secret")
                            .addConstraintViolation()
                        return false
                    }
                    true
                }
                AccessTokenRequest.GrantTypeEnum.AUTHORIZATION_CODE -> {
                    if (value.clientId == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_id field when using the authorization_code grant_type.")
                            .addPropertyNode("client_id")
                            .addConstraintViolation()
                        return false
                    }
                    if (value.code == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a code field when using the authorization_code grant_type.")
                            .addPropertyNode("code")
                            .addConstraintViolation()
                        return false
                    }
                    if (value.redirectUri == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a redirect_uri field when using the authorization_code grant_type.")
                            .addPropertyNode("redirect_uri")
                            .addConstraintViolation()
                        return false
                    }
                    true
                }
                AccessTokenRequest.GrantTypeEnum.REFRESH_TOKEN -> {
                    if (value.refreshToken.isNullOrEmpty()) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a refresh_token field when using the refresh_token grant_type.")
                            .addPropertyNode("refresh_token")
                            .addConstraintViolation()
                        return false
                    }
                    if (value.clientId == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_id field when using the refresh_token grant_type.")
                            .addPropertyNode("client_id")
                            .addConstraintViolation()
                        return false
                    }
                    true
                }
                else -> false
            }
        }
    }
}
