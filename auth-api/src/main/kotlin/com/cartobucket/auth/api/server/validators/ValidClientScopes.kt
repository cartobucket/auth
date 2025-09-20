// (C)2024
package com.cartobucket.auth.api.server.validators

import com.cartobucket.auth.api.dto.ClientRequest
import com.cartobucket.auth.data.domain.Page
import com.cartobucket.auth.data.domain.Scope
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
@Constraint(validatedBy = [ValidClientScopes.Validator::class])
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
annotation class ValidClientScopes(
    val message: String = "scopes must exist on the Authorization Server",
    val payload: Array<KClass<out Payload>> = [],
    val groups: Array<KClass<*>> = [],
) {
    class Validator : ConstraintValidator<ValidClientScopes, ClientRequest> {
        @Inject
        lateinit var scopeService: ScopeService

        override fun initialize(constraintAnnotation: ValidClientScopes) {
            super.initialize(constraintAnnotation)
        }

        override fun isValid(
            value: ClientRequest?,
            context: ConstraintValidatorContext,
        ): Boolean {
            if (value?.authorizationServerId == null || value.scopes == null) {
                return false
            }

            val scopes =
                scopeService
                    .getScopes(
                        listOf(value.authorizationServerId),
                        // TODO: This should probably get moved into the service, at the very least, this needs
                        //  to be iterated over until no scopes are left.
                        Page(100, 0),
                    ).map(Scope::id)
            return scopes.toSet().containsAll(value.scopes)
        }
    }
}
