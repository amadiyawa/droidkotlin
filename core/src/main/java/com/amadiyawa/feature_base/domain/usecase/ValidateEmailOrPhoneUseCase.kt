package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.droidkotlin.core.R

class ValidateEmailOrPhoneUseCase(
    private val validateEmail: ValidateEmailUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val context: Context
) {
    fun execute(input: String): FieldValidationResult {
        return when {
            validateEmail.execute(input).isValid -> FieldValidationResult(true)
            validatePhone.execute(input).isValid -> FieldValidationResult(true)
            else -> FieldValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.invalid_identifier)
            )
        }
    }
}