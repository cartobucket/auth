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

package com.cartobucket.auth.authorization.server.validators;

import com.cartobucket.auth.model.generated.AccessTokenRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static jakarta.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT;
import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidAccessTokenRequest.Validator.class })
@Target(value = {
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER,
        TYPE_USE})
@SupportedValidationTarget(ANNOTATED_ELEMENT)
@Documented
public @interface ValidAccessTokenRequest {
    String message() default "The Access Token request is not valid";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
    public class Validator implements ConstraintValidator<ValidAccessTokenRequest, AccessTokenRequest> {
        @Override
        public void initialize(ValidAccessTokenRequest constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(AccessTokenRequest value, ConstraintValidatorContext context) {
            if (value.getGrantType() == null) {
                context.buildConstraintViolationWithTemplate("The Access Token request must contain a grant_type field.").addPropertyNode("grant_type").addConstraintViolation();
                return false;
            }

            switch (value.getGrantType()) {
                case CLIENT_CREDENTIALS -> {
                    if (value.getClientId() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_id field when using the client_credentials grant_type.").addPropertyNode("client_id").addConstraintViolation();
                        return false;
                    }
                    if (value.getClientSecret() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_secret field when using the client_credentials grant_type.").addPropertyNode("client_secret").addConstraintViolation();
                        return false;
                    }
                    return true;
                }
                case AUTHORIZATION_CODE -> {
                    if (value.getClientId() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_id field when using the authorization_code grant_type.").addPropertyNode("client_id").addConstraintViolation();
                        return false;
                    }
                    if (value.getCode() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a code field when using the authorization_code grant_type.").addPropertyNode("code").addConstraintViolation();
                        return false;
                    }
                    if (value.getRedirectUri() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a redirect_uri field when using the authorization_code grant_type.").addPropertyNode("redirect_uri").addConstraintViolation();
                        return false;
                    }
                    // TODO: The code_verifier should be the only required field but it is not supported yet in the mozilla django oidc package.
                    if (value.getClientSecret() == null && value.getCodeVerifier() == null) {
                        context.buildConstraintViolationWithTemplate("The Access Token request must contain a client_secret or a code_verifier field when using the authorization_code grant_type.").addPropertyNode("code_verifier").addConstraintViolation();
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }
    }
}

