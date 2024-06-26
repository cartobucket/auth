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

package com.cartobucket.auth.api.server.validators;

import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.AuthorizationServerService;
import com.cartobucket.auth.model.generated.ApplicationRequest;
import jakarta.inject.Inject;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import static jakarta.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT;
import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidApplicationRequest.Validator.class })
@Target(value = {
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER,
        TYPE_USE})
@SupportedValidationTarget(ANNOTATED_ELEMENT)
@Documented
public @interface ValidApplicationRequest {
    String message() default "The Application was not valid";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
    public class Validator implements ConstraintValidator<ValidApplicationRequest, ApplicationRequest> {
        @Inject
        ApplicationService applicationService;
        @Inject
        AuthorizationServerService authorizationServerService;

        @Override
        public void initialize(ValidApplicationRequest constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(ApplicationRequest value, ConstraintValidatorContext context) {
            try {
                 final var authorizationServer = authorizationServerService.getAuthorizationServer(
                         value.getAuthorizationServerId()
                 );
                 return authorizationServer
                         .getScopes()
                         .stream()
                         .map(Scope::getId)
                         .toList()
                         .containsAll(value.getScopes());
            } catch (Exception e) {
                return false;
            }
        }
    }
}

