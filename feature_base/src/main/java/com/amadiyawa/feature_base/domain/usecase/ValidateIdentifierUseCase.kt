package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.droidkotlin.base.R

/**
 * Use case for validating an identifier.
 *
 * This class provides functionality to validate an input string as an identifier.
 * It supports validation for email, username, and phone number using the respective
 * validation use cases. If the input does not match any valid identifier format,
 * an error message is returned.
 *
 * @property validateEmail Use case for validating email addresses.
 * @property validateUsername Use case for validating usernames.
 * @property validatePhone Use case for validating phone numbers.
 * @property context Android context used to retrieve localized error messages.
 */
class ValidateIdentifierUseCase(
    private val validateEmail: ValidateEmailUseCase,
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePhone: ValidatePhoneUseCase,
    private val context: Context
) {

    /**
     * Executes the identifier validation process.
     *
     * This method validates the given input string against multiple identifier formats
     * (email, username, and phone number). It returns a `FieldValidationResult` indicating
     * whether the input is valid or not. If the input is invalid, an error message is included.
     *
     * @param input The input string to be validated.
     * @return A `FieldValidationResult` object containing the validation result and an optional error message.
     */
    fun execute(input: String): FieldValidationResult {
        return when {
            validateEmail.execute(input).isValid -> FieldValidationResult(true)
            validateUsername.execute(input).isValid -> FieldValidationResult(true)
            validatePhone.execute(input).isValid -> FieldValidationResult(true)
            else -> FieldValidationResult(
                isValid = false,
                errorMessage = context.getString(R.string.invalid_identifier)
            )
        }
    }
}