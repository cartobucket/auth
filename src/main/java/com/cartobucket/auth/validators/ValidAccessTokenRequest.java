package com.cartobucket.auth.validators;

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
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

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

