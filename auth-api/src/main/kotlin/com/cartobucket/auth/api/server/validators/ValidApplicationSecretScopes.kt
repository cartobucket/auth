// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.api.dto.ApplicationSecretRequest
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
import com.cartobucket.auth.data.services.ApplicationService
import com.cartobucket.auth.data.services.ScopeService
import jakarta.inject.Inject
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraintvalidation.SupportedValidationTarget
import jakarta.validation.constraintvalidation.ValidationTarget
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidApplicationSecretScopes.Validator::class])
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
annotation class ValidApplicationSecretScopes(
    val message: String = "scopes must exist on the Authorization Server",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidApplicationSecretScopes, ApplicationSecretRequest> {
        @Inject
        lateinit var scopeService: ScopeService

        @Inject
        lateinit var applicationService: ApplicationService

        override fun initialize(constraintAnnotation: ValidApplicationSecretScopes) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: ApplicationSecretRequest?,
            context: ConstraintValidatorContext,
        ): Boolean =
            value?.let {
                val application = applicationService.getApplication(it.applicationId)
                val scopes =
                    scopeService
                        .getScopes(
                            listOfNotNull(
                                application.left!!.authorizationServerId,
                            ),
                            // TODO: This should probably get moved into the service, at the very least, this needs
                            //  to be iterated over until no scopes are left.
                            Page(100, 0),
                        ).mapNotNull(Scope::id)
                scopes.toSet().containsAll(it.scopes)
            } ?: false
    }
}
