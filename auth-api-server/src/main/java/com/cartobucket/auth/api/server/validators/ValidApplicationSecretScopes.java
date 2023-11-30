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

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Scope;
import com.cartobucket.auth.data.services.ApplicationService;
import com.cartobucket.auth.data.services.ScopeService;
import com.cartobucket.auth.model.generated.ApplicationSecretRequest;
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
import java.util.List;

import static jakarta.validation.constraintvalidation.ValidationTarget.ANNOTATED_ELEMENT;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidApplicationSecretScopes.Validator.class })
@Target(value = {
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER,
        TYPE_USE})
@SupportedValidationTarget(ANNOTATED_ELEMENT)
@Documented
public @interface ValidApplicationSecretScopes {
    String message() default "scopes must exist on the Authorization Server";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
    public class Validator implements ConstraintValidator<ValidApplicationSecretScopes, ApplicationSecretRequest> {
        @Inject
        ScopeService scopeService;
        @Inject
        ApplicationService applicationService;

        @Override
        public void initialize(ValidApplicationSecretScopes constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(ApplicationSecretRequest value, ConstraintValidatorContext context) {
            final var application = applicationService.getApplication(value.getApplicationId());
            final var scopes = scopeService
                    .getScopes(
                            List.of(
                                    application.getLeft().getAuthorizationServerId()
                            ),
                            // TODO: This should probably get moved into the service, at the very least, this needs
                            //  to be iterated over until no scopes are left.
                            new Page(100, 0))
                    .stream()
                    .map(Scope::getName)
                    .toList();
            return scopes.containsAll(ScopeService.scopeStringToScopeList(value.getScopes()));
        }
    }
}

