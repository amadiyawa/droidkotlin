package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.common.util.PhoneNumberValidator
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.validation.ValidationPatterns
import com.amadiyawa.paygo.base.R

/**
 * Use case for validating user identifiers such as email, username, or phone number.
 *
 * This class provides a method to validate an input string against predefined patterns
 * for email, username, or phone number. If the input does not match any of these patterns,
 * it returns a validation failure with an appropriate error message.
 *
 * @property context The Android context used to retrieve localized error messages.
 */
class ValidateIdentifierUseCase(private val context: Context) {

    /**
     * Executes the validation logic for the given input.
     *
     * @param input The identifier to validate (e.g., email, username, or phone number).
     * @return A [FieldValidationResult] indicating whether the input is valid.
     *         If invalid, it includes an error message.
     */
    fun execute(input: String): FieldValidationResult {
        return when {
            input.matches(ValidationPatterns.EMAIL) -> FieldValidationResult(true)
            input.matches(ValidationPatterns.USERNAME) -> FieldValidationResult(true)
            PhoneNumberValidator.isValid(input) -> FieldValidationResult(true)
            else -> FieldValidationResult(false, context.getString(R.string.invalid_identifier))
        }
    }
}