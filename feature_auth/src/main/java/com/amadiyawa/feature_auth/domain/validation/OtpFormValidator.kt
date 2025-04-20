package com.amadiyawa.feature_auth.domain.validation

import com.amadiyawa.feature_auth.domain.model.OtpForm
import com.amadiyawa.feature_base.domain.model.ValidatedForm
import com.amadiyawa.feature_base.domain.usecase.ValidateOtpUseCase

class OtpFormValidator(private val validateOtpUseCase: ValidateOtpUseCase) {
    fun validate(form: OtpForm): ValidatedForm {
        val validatedDigits = form.digits.map { digit ->
            digit.copy(validation = validateOtpUseCase.validateDigit(digit.value))
        }

        return ValidatedForm(
            fields = validatedDigits.mapIndexed { index, field ->
                "digit$index" to field
            }.toMap()
        )
    }
}