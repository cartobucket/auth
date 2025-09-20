// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.data.services.ApplicationService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import java.util.UUID
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplication.Validator::class])
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
annotation class ValidApplication(
    val message: String = "The Application was not found",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidApplication, UUID> {
        @Inject
        lateinit var applicationService: ApplicationService

        override fun initialize(constraintAnnotation: ValidApplication) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: UUID?,
            context: ConstraintValidatorContext,
        ): Boolean =
            try {
                value?.let { applicationService.getApplication(it) }
                true
            } catch (e: Exception) {
                false
            }
    }
}
