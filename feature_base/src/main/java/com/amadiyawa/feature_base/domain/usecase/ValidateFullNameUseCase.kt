package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.validation.ValidationPatterns
import com.amadiyawa.droidkotlin.base.R

class ValidateFullNameUseCase(private val context: Context) {
    fun execute(input: String): FieldValidationResult {
        return if (input.matches(ValidationPatterns.FULL_NAME)) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.invalid_full_name))
        }
    }
}