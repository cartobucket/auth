// (C)2024
package com.cartobucket.auth.api.server.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import java.net.MalformedURLException
import java.net.URI
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidAuthorizationServerUrl.Validator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@MustBeDocumented
annotation class ValidAuthorizationServerUrl(
    val message: String = "must be in the form of https://company.example",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidAuthorizationServerUrl, String> {
        override fun initialize(constraintAnnotation: ValidAuthorizationServerUrl) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: String?,
            context: ConstraintValidatorContext,
        ): Boolean =
            try {
                if (value == null) {
                    throw IllegalArgumentException()
                }
                URI.create(value).toURL()
                true
            } catch (e: MalformedURLException) {
                context.buildConstraintViolationWithTemplate("")
                false
            } catch (e: IllegalArgumentException) {
                context.buildConstraintViolationWithTemplate("")
                false
            }
    }
}
