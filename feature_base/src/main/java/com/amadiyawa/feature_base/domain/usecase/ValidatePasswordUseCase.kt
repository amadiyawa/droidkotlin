package com.amadiyawa.feature_base.domain.usecase

import android.content.Context
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.validation.ValidationPatterns
import com.amadiyawa.droidkotlin.base.R


/**
 * Use case for validating passwords based on predefined rules.
 *
 * @param context The application context used to access string resources.
 * @author Amadou Iyawa
 */
class ValidatePasswordUseCase(private val context: Context) {

    /**
     * Validates a password based on the following rules:
     * - Minimum length
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     * - No spaces allowed
     *
     * @param password The password to validate.
     * @return A [FieldValidationResult] indicating whether the password is valid or not.
     *
     * @author Amadou Iyawa
     */
    fun execute(password: String): FieldValidationResult {
        val rules = listOf(
            ::validateLength,
            ::validateUppercase,
            ::validateLowercase,
            ::validateDigit,
            ::validateSpecialCharacter,
            ::validateNoSpaces
        )

        for (rule in rules) {
            val result = rule(password)
            if (!result.isValid) return result
        }

        return FieldValidationResult(true)
    }

    private fun validateLength(password: String): FieldValidationResult {
        return if (password.length < ValidationPatterns.PASSWORD_MIN_LENGTH) {
            FieldValidationResult(
                false,
                context.getString(R.string.error_password_length, ValidationPatterns.PASSWORD_MIN_LENGTH)
            )
        } else FieldValidationResult(true)
    }

    private fun validateUppercase(password: String): FieldValidationResult {
        return if (!password.any { it.isUpperCase() }) {
            FieldValidationResult(false, context.getString(R.string.add_uppercase))
        } else FieldValidationResult(true)
    }

    private fun validateLowercase(password: String): FieldValidationResult {
        return if (!password.any { it.isLowerCase() }) {
            FieldValidationResult(false, context.getString(R.string.add_lowercase))
        } else FieldValidationResult(true)
    }

    private fun validateDigit(password: String): FieldValidationResult {
        return if (!password.any { it.isDigit() }) {
            FieldValidationResult(false, context.getString(R.string.add_digit))
        } else FieldValidationResult(true)
    }

    private fun validateSpecialCharacter(password: String): FieldValidationResult {
        return if (!password.any { "!@#$%^&*()_-+=<>?/{}~|".contains(it) }) {
            FieldValidationResult(false, context.getString(R.string.add_special_character))
        } else FieldValidationResult(true)
    }

    private fun validateNoSpaces(password: String): FieldValidationResult {
        return if (password.contains(" ")) {
            FieldValidationResult(false, context.getString(R.string.error_password_space))
        } else FieldValidationResult(true)
    }
}