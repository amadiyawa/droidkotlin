package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.common.util.PhoneNumberValidator
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.droidkotlin.base.R

class ValidatePhoneUseCase(private val context: Context) {
    fun execute(input: String): FieldValidationResult {
        return if (PhoneNumberValidator.isValid(input)) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.invalid_phone))
        }
    }
}