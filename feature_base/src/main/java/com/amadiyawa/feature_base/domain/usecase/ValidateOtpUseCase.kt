package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.paygo.base.R

class ValidateOtpUseCase(private val context: Context) {

    fun validateDigit(digit: String): FieldValidationResult {
        return if (digit.length == 1 && digit.all { it.isDigit() }) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.invalid_otp_digit))
        }
    }

    fun validateMatch(input: String, expected: String): FieldValidationResult {
        return if (input == expected) {
            FieldValidationResult(true)
        } else {
            FieldValidationResult(false, context.getString(R.string.otp_code_mismatch))
        }
    }
}