package com.cartobucket.auth.validators;

import com.cartobucket.auth.repositories.AuthorizationServerRepository;
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
@Constraint(validatedBy = { ValidAuthorizationServerRequest.Validator.class })
@Target(value = {
        METHOD,
        FIELD,
        ANNOTATION_TYPE,
        CONSTRUCTOR,
        PARAMETER,
        TYPE_USE})
@SupportedValidationTarget(ANNOTATED_ELEMENT)
@Documented
public @interface ValidAuthorizationServerRequest {
    String message() default "The Authorization Server was not found";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
    public class Validator implements ConstraintValidator<ValidAuthorizationServerRequest, UUID> {
        @Inject
        AuthorizationServerRepository authorizationServerRepository;

        @Override
        public void initialize(ValidAuthorizationServerRequest constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(UUID value, ConstraintValidatorContext context) {
            final var authorizationServer = authorizationServerRepository.findById(value);
            if (authorizationServer.isEmpty()) {
                context
                        .buildConstraintViolationWithTemplate("The Authorization Server was not found.")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}

