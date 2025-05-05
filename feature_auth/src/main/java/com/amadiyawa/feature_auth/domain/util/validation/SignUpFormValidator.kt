package com.amadiyawa.feature_auth.domain.util.validation

import com.amadiyawa.feature_auth.domain.model.SignUpForm
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.usecase.ValidateEmailUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateFullNameUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordConfirmationUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePasswordUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidatePhoneUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateTermsAcceptedUseCase
import com.amadiyawa.feature_base.domain.usecase.ValidateUsernameUseCase

class SignUpFormValidator(
    val validateFullName: ValidateFullNameUseCase,
    val validateUsername: ValidateUsernameUseCase,
    val validateEmail: ValidateEmailUseCase,
    val validatePhone: ValidatePhoneUseCase,
    val validatePassword: ValidatePasswordUseCase,
    val validatePasswordConfirmation: ValidatePasswordConfirmationUseCase,
    val validateTermsAccepted: ValidateTermsAcceptedUseCase
) {

    fun validate(form: SignUpForm): ValidatedForm {
        return ValidatedForm(
            fields = mapOf(
                "fullName" to form.fullName.copy(validation = validateFullName.execute(form.fullName.value)),
                "username" to form.username.copy(validation = validateUsername.execute(form.username.value)),
                "email" to form.email.copy(validation = validateEmail.execute(form.email.value)),
                "phone" to form.phone.copy(validation = validatePhone.execute(form.phone.value)),
                "password" to form.password.copy(validation = validatePassword.execute(form.password.value)),
                "confirmPassword" to form.confirmPassword.copy(
                    validation = validatePasswordConfirmation.execute(
                        password = form.password.value,
                        confirmPassword = form.confirmPassword.value
                    )
                ),
                "termsAccepted" to form.termsAccepted.copy(
                    validation = validateTermsAccepted.execute(form.termsAccepted.value)
                )
            )
        )
    }
}