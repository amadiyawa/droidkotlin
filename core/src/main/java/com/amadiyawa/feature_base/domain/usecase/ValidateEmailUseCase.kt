package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.validation.ValidationPatterns
import com.droidkotlin.core.R

class ValidateEmailUseCase(private val context: Context) {
    fun execute(input: String): FieldValidationResult {
        return if (input.matches(ValidationPatterns.EMAIL)) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.invalid_email))
        }
    }
}