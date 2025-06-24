package com.amadiyawa.feature_base.domain.model

import com.droidkotlin.core.R
import com.amadiyawa.feature_base.common.resources.StringResourceProvider

/**
 * Reusable password validation utility that can be used across all modules.
 *
 * This object provides comprehensive password validation following industry
 * standards and security best practices.
 *
 * @author Amadou Iyawa
 */
object PasswordValidator {

    // Password validation constants based on security standards
    private const val MIN_LENGTH_DEFAULT = 8
    private const val MIN_LENGTH_STRONG = 12
    private const val MIN_LENGTH_BASIC = 6
    private const val MAX_LENGTH_DEFAULT = 128
    private const val MAX_LENGTH_STRICT = 64

    // Special characters based on OWASP recommendations
    private const val SPECIAL_CHARS_DEFAULT = "!@#$%^&*()_-+=<>?/{}~|"
    private const val SPECIAL_CHARS_EXTENDED = "!@#$%^&*()_-+=<>?/{}~|[]\\:;\"'<>,.?"

    /**
     * Password validation configuration that can be customized per use case.
     */
    data class PasswordRules(
        val minLength: Int = MIN_LENGTH_DEFAULT,
        val maxLength: Int = MAX_LENGTH_DEFAULT,
        val requireUppercase: Boolean = true,
        val requireLowercase: Boolean = true,
        val requireDigit: Boolean = true,
        val requireSpecialChar: Boolean = true,
        val allowSpaces: Boolean = false,
        val customSpecialChars: String = SPECIAL_CHARS_DEFAULT
    )

    /**
     * Predefined password rule sets following common standards.
     */
    object Rules {

        /**
         * Default rules following NIST SP 800-63B guidelines.
         * - Minimum 8 characters
         * - All character types required
         * - No spaces allowed
         */
        val DEFAULT = PasswordRules()

        /**
         * Strong password rules for high-security contexts.
         * - Minimum 12 characters
         * - All character types required
         * - Extended special character set
         * - Shorter max length for better security
         */
        val STRONG = PasswordRules(
            minLength = MIN_LENGTH_STRONG,
            maxLength = MAX_LENGTH_STRICT,
            customSpecialChars = SPECIAL_CHARS_EXTENDED
        )

        /**
         * Basic rules for less critical contexts.
         * - Minimum 6 characters
         * - Only lowercase and digit required
         * - More permissive
         */
        val BASIC = PasswordRules(
            minLength = MIN_LENGTH_BASIC,
            requireUppercase = false,
            requireSpecialChar = false,
            allowSpaces = true
        )

        /**
         * Sign-in rules - more lenient for existing users.
         * - Minimum 6 characters (to accommodate existing accounts)
         * - All character types recommended but not enforced strictly
         */
        val SIGNIN = PasswordRules(
            minLength = MIN_LENGTH_BASIC,
            requireSpecialChar = false // Many existing accounts may not have special chars
        )

        /**
         * Registration rules - stricter for new accounts.
         * - Minimum 8 characters
         * - All character types required
         * - Following modern security standards
         */
        val REGISTRATION = PasswordRules()

        /**
         * Admin/privileged account rules.
         * - Minimum 12 characters
         * - All character types required
         * - Extended special characters
         * - Stricter length limits
         */
        val ADMIN = PasswordRules(
            minLength = MIN_LENGTH_STRONG,
            maxLength = MAX_LENGTH_STRICT,
            customSpecialChars = SPECIAL_CHARS_EXTENDED
        )

        /**
         * Temporary password rules (for password reset, etc.).
         * - Shorter minimum length for temporary use
         * - Still secure but more practical
         */
        val TEMPORARY = PasswordRules(
            minLength = MIN_LENGTH_BASIC,
            requireUppercase = false,
            allowSpaces = true
        )
    }

    /**
     * Validates a password with default rules.
     *
     * @param password The password to validate
     * @param stringProvider Provider for string resources
     * @return FieldValidationResult indicating validation success or failure with error message
     */
    fun validate(
        password: String,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return validate(password, Rules.DEFAULT, stringProvider)
    }

    /**
     * Validates a password with custom rules.
     *
     * @param password The password to validate
     * @param rules Custom password rules to apply
     * @param stringProvider Provider for string resources
     * @return FieldValidationResult indicating validation success or failure with error message
     */
    fun validate(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        if (password.isBlank()) {
            return FieldValidationResult.invalid(
                stringProvider.getString(R.string.password_required)
            )
        }

        // Apply validation rules based on configuration
        val validationChecks = mutableListOf<() -> FieldValidationResult>()

        // Always check length and max length
        validationChecks.add { validateLength(password, rules, stringProvider) }
        validationChecks.add { validateMaxLength(password, rules, stringProvider) }

        // Only add space validation if spaces are not allowed
        if (!rules.allowSpaces) {
            validationChecks.add { validateSpaces(password, rules, stringProvider) }
        }

        // Only add character type validations if required
        if (rules.requireUppercase) {
            validationChecks.add { validateUppercase(password, rules, stringProvider) }
        }
        if (rules.requireLowercase) {
            validationChecks.add { validateLowercase(password, rules, stringProvider) }
        }
        if (rules.requireDigit) {
            validationChecks.add { validateDigit(password, rules, stringProvider) }
        }
        if (rules.requireSpecialChar) {
            validationChecks.add { validateSpecialCharacter(password, rules, stringProvider) }
        }

        // Return first validation error found
        for (check in validationChecks) {
            val result = check()
            if (!result.isValid) return result
        }

        return FieldValidationResult.Valid
    }

    /**
     * Quick validation that only returns boolean (useful for UI state checks).
     */
    fun isValid(password: String, rules: PasswordRules = Rules.DEFAULT): Boolean {
        if (password.isBlank()) return false

        return when {
            password.length < rules.minLength -> false
            password.length > rules.maxLength -> false
            !rules.allowSpaces && password.contains(" ") -> false
            rules.requireUppercase && !password.any { it.isUpperCase() } -> false
            rules.requireLowercase && !password.any { it.isLowerCase() } -> false
            rules.requireDigit && !password.any { it.isDigit() } -> false
            rules.requireSpecialChar && !password.any { rules.customSpecialChars.contains(it) } -> false
            else -> true
        }
    }

    /**
     * Get password strength score (0-100).
     */
    fun getStrengthScore(password: String, rules: PasswordRules = Rules.DEFAULT): Int {
        if (password.isBlank()) return 0

        var score = 0

        // Length scoring (40% of total score)
        score += when {
            password.length >= rules.minLength + 4 -> 40
            password.length >= rules.minLength + 2 -> 30
            password.length >= rules.minLength -> 20
            else -> (password.length * 20) / rules.minLength
        }

        // Character variety scoring (60% of total score)
        if (password.any { it.isUpperCase() }) score += 15
        if (password.any { it.isLowerCase() }) score += 15
        if (password.any { it.isDigit() }) score += 15
        if (password.any { rules.customSpecialChars.contains(it) }) score += 15

        return score.coerceIn(0, 100)
    }

    /**
     * Get list of missing requirements for better UX feedback.
     */
    fun getMissingRequirements(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): List<String> {
        val missing = mutableListOf<String>()

        if (password.length < rules.minLength) {
            missing.add(stringProvider.getString(R.string.error_password_length, rules.minLength))
        }
        if (password.length > rules.maxLength) {
            missing.add(stringProvider.getString(R.string.error_password_too_long, rules.maxLength))
        }
        if (rules.requireUppercase && !password.any { it.isUpperCase() }) {
            missing.add(stringProvider.getString(R.string.add_uppercase))
        }
        if (rules.requireLowercase && !password.any { it.isLowerCase() }) {
            missing.add(stringProvider.getString(R.string.add_lowercase))
        }
        if (rules.requireDigit && !password.any { it.isDigit() }) {
            missing.add(stringProvider.getString(R.string.add_digit))
        }
        if (rules.requireSpecialChar && !password.any { rules.customSpecialChars.contains(it) }) {
            missing.add(stringProvider.getString(R.string.add_special_character))
        }
        if (!rules.allowSpaces && password.contains(" ")) {
            missing.add(stringProvider.getString(R.string.error_password_space))
        }

        return missing
    }

    // Private validation methods
    private fun validateLength(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (password.length < rules.minLength) {
            FieldValidationResult.invalid(
                stringProvider.getString(R.string.error_password_length, rules.minLength)
            )
        } else FieldValidationResult.Valid
    }

    private fun validateMaxLength(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (password.length > rules.maxLength) {
            FieldValidationResult.invalid(
                stringProvider.getString(R.string.error_password_too_long, rules.maxLength)
            )
        } else FieldValidationResult.Valid
    }

    private fun validateUppercase(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (rules.requireUppercase && !password.any { it.isUpperCase() }) {
            FieldValidationResult.invalid(stringProvider.getString(R.string.add_uppercase))
        } else FieldValidationResult.Valid
    }

    private fun validateLowercase(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (rules.requireLowercase && !password.any { it.isLowerCase() }) {
            FieldValidationResult.invalid(stringProvider.getString(R.string.add_lowercase))
        } else FieldValidationResult.Valid
    }

    private fun validateDigit(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (rules.requireDigit && !password.any { it.isDigit() }) {
            FieldValidationResult.invalid(stringProvider.getString(R.string.add_digit))
        } else FieldValidationResult.Valid
    }

    private fun validateSpecialCharacter(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (rules.requireSpecialChar && !password.any { rules.customSpecialChars.contains(it) }) {
            FieldValidationResult.invalid(stringProvider.getString(R.string.add_special_character))
        } else FieldValidationResult.Valid
    }

    private fun validateSpaces(
        password: String,
        rules: PasswordRules,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return if (!rules.allowSpaces && password.contains(" ")) {
            FieldValidationResult.invalid(stringProvider.getString(R.string.error_password_space))
        } else FieldValidationResult.Valid
    }
}