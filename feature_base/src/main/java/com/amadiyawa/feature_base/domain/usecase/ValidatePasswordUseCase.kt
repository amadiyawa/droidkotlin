package com.amadiyawa.feature_base.domain.usecase

import com.amadiyawa.feature_base.domain.validation.ValidationPatterns

/**
 * Use case for validating a password based on specific criteria.
 */
class ValidatePasswordUseCase {

    /**
     * Executes the password validation.
     *
     * @param password The password to validate.
     * @return `true` if the password meets all the criteria, `false` otherwise.
     */
    fun execute(password: String): Boolean {
        val minLength = ValidationPatterns.passwordMinLength
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return password.length >= minLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar
    }
}