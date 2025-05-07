package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.droidkotlin.base.R
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.validation.ValidationPatterns

class ValidateUsernameUseCase(private val context: Context) {
    fun execute(input: String): FieldValidationResult {
        return if (input.matches(ValidationPatterns.USERNAME)) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.invalid_username))
        }
    }
}