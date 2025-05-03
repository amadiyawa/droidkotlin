package com.amadiyawa.feature_auth.domain.validation

import com.amadiyawa.feature_auth.domain.model.ResetPasswordForm
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordConfirmationUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase

class ResetPasswordValidator (
    val validatePassword: ValidatePasswordUseCase,
    val validatePasswordConfirmation: ValidatePasswordConfirmationUseCase
) {

    fun validate(form: ResetPasswordForm): ValidatedForm {
        return ValidatedForm(
            fields = mapOf(
                "password" to form.password.copy(
                    validation = validatePassword.execute(form.password.value)
                ),
                "confirmPassword" to form.confirmPassword.copy(
                    validation = validatePasswordConfirmation.execute(
                        password = form.password.value,
                        confirmPassword = form.confirmPassword.value
                    )
                )
            )
        )
    }
}