package com.amadiyawa.feature_auth.domain.model

import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.amadiyawa.feature_base.domain.model.EmailValidator
import com.amadiyawa.feature_base.domain.model.FieldValidationResult
import com.amadiyawa.feature_base.domain.model.PasswordValidator
import com.amadiyawa.feature_base.domain.model.PhoneNumberValidator
import com.amadiyawa.feature_base.domain.model.UsernameValidator
import com.amadiyawa.feature_base.domain.model.ValidatedField
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.amadiyawa.droidkotlin.base.R as BaseR

/**
 * Represents a sign-in form with intelligent identifier validation.
 *
 * This data class provides smart validation for login scenarios where users
 * can enter email, username, or phone number (with or without country code).
 * Phone numbers are intelligently validated with local number support.
 *
 * @property identifier A validated field for the user's identifier (email/username/phone).
 * @property password A validated field for the user's password.
 * @property rememberMe Whether to remember the user's login state.
 */
data class SignInForm(
    val identifier: ValidatedField<String> = ValidatedField(""),
    val password: ValidatedField<String> = ValidatedField(value = "", isValueHidden = true),
    val rememberMe: Boolean = false
) {

    // Form completion check
    val isComplete: Boolean
        get() = identifier.value.isNotBlank() && password.value.isNotBlank()

    // Form validity check
    val isValid: Boolean
        get() = identifier.validation.isValid && password.validation.isValid && isComplete

    // Check if form can be submitted
    val canSubmit: Boolean
        get() = isValid && identifier.isTouched && password.isTouched

    /**
     * Universal field update function that handles both identifier and password fields.
     *
     * @param field The field to update ("identifier" or "password")
     * @param value The new value for the field
     * @param stringProvider Provider for string resources
     * @param validateImmediately Whether to validate immediately or wait for user interaction
     * @return Updated form with the specified field modified
     */
    fun updateField(
        field: String,
        value: String,
        stringProvider: StringResourceProvider,
        validateImmediately: Boolean = false
    ): SignInForm {
        return when (field) {
            "identifier" -> updateIdentifierInternal(value, stringProvider, validateImmediately)
            "password" -> updatePasswordInternal(value, stringProvider, validateImmediately)
            else -> this
        }
    }

    // Batch update multiple fields at once
    fun updateFields(
        updates: Map<String, String>,
        stringProvider: StringResourceProvider,
        validateImmediately: Boolean = false
    ): SignInForm {
        return updates.entries.fold(this) { form, (field, value) ->
            form.updateField(field, value, stringProvider, validateImmediately)
        }
    }

    // Toggle password visibility
    fun togglePasswordVisibility(): SignInForm {
        return copy(
            password = password.copy(isValueHidden = !password.isValueHidden)
        )
    }

    // Toggle remember me state
    fun toggleRememberMe(): SignInForm {
        return copy(rememberMe = !rememberMe)
    }

    // Internal identifier update with optimized validation
    private fun updateIdentifierInternal(
        value: String,
        stringProvider: StringResourceProvider,
        validateImmediately: Boolean
    ): SignInForm {
        val shouldValidate = validateImmediately || identifier.isTouched || value.isBlank()

        return copy(
            identifier = identifier.copy(
                value = value,
                validation = if (shouldValidate) validateIdentifier(value, stringProvider) else identifier.validation,
                isTouched = identifier.isTouched || value.isNotBlank()
            )
        )
    }

    // Internal password update with optimized validation
    private fun updatePasswordInternal(
        value: String,
        stringProvider: StringResourceProvider,
        validateImmediately: Boolean
    ): SignInForm {
        val shouldValidate = validateImmediately || password.isTouched || value.isBlank()

        return copy(
            password = password.copy(
                value = value,
                validation = if (shouldValidate) validatePassword(value, stringProvider) else password.validation,
                isTouched = password.isTouched || value.isNotBlank()
            )
        )
    }

    /**
     * Smart identifier validation using professional validators.
     * Handles local phone numbers intelligently for login scenarios.
     */
    private fun validateIdentifier(
        value: String,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        if (value.isBlank()) {
            return FieldValidationResult.invalid(
                stringProvider.getString(BaseR.string.identifier_required)
            )
        }

        val cleanValue = value.trim()
        val detectedType = detectIdentifierType(cleanValue)

        return when (detectedType) {
            IdentifierType.EMAIL -> {
                val result = EmailValidator.validateDetailed(
                    input = cleanValue,
                    stringProvider = stringProvider
                )
                if (result.isValid) {
                    FieldValidationResult.Valid
                } else {
                    FieldValidationResult.invalid(
                        result.errorReason ?: stringProvider.getString(BaseR.string.invalid_email)
                    )
                }
            }

            IdentifierType.PHONE -> {
                // For login, we're more lenient - allow local numbers
                val result = PhoneNumberValidator.validateMobile(cleanValue, stringProvider = stringProvider)
                if (result.isValid) {
                    FieldValidationResult.Valid
                } else {
                    // For login, if it looks like a phone but validation fails,
                    // still allow it (user might have entered local format correctly)
                    if (PhoneNumberValidator.Utils.looksLikePhoneNumber(cleanValue)) {
                        FieldValidationResult.Valid
                    } else {
                        FieldValidationResult.invalid(
                            result.errorReason ?: stringProvider.getString(BaseR.string.invalid_phone)
                        )
                    }
                }
            }

            IdentifierType.USERNAME -> {
                val result = UsernameValidator.validateDetailed(
                    input = cleanValue,
                    stringProvider = stringProvider
                )
                if (result.isValid) {
                    FieldValidationResult.Valid
                } else {
                    FieldValidationResult.invalid(
                        result.errorReason ?: stringProvider.getString(BaseR.string.invalid_username)
                    )
                }
            }
        }
    }

    /**
     * Intelligent identifier type detection using professional validator utilities.
     * Optimized for login scenarios with smart phone number detection.
     */
    private fun detectIdentifierType(input: String): IdentifierType {
        val cleanInput = input.trim()

        return when {
            // Email detection is most reliable (contains @)
            EmailValidator.Utils.looksLikeEmail(cleanInput) -> IdentifierType.EMAIL

            // Phone detection with login-optimized logic
            isLikelyPhoneForLogin(cleanInput) -> IdentifierType.PHONE

            // Username detection using professional validator
            UsernameValidator.Utils.looksLikeUsername(cleanInput) -> IdentifierType.USERNAME

            // Fallback to username for anything else
            else -> IdentifierType.USERNAME
        }
    }

    /**
     * Login-optimized phone detection that's more permissive for local numbers.
     */
    private fun isLikelyPhoneForLogin(input: String): Boolean {
        val digits = input.replace(Regex("[^0-9]"), "")

        return when {
            // Too short to be any phone number
            digits.length < 7 -> false

            // Clear international format
            input.startsWith("+") && digits.length >= 8 -> true

            // Formatted phone patterns (very likely phone)
            input.matches(Regex(".*[\\(\\)\\-\\s].*")) && digits.length >= 8 -> true

            // Local mobile patterns common in many countries
            input.startsWith("0") && digits.length >= 9 -> true  // 0675123456
            input.startsWith("6") && digits.length == 9 -> true  // 675123456 (Cameroon mobile)
            input.startsWith("7") && digits.length >= 9 -> true  // UK/other mobile
            input.startsWith("8") && digits.length >= 9 -> true  // Various mobile
            input.startsWith("9") && digits.length >= 9 -> true  // Various mobile

            // Long digit strings (10+ digits very likely phone)
            digits.length >= 10 -> true

            // Use professional validator for additional patterns
            PhoneNumberValidator.Utils.looksLikePhoneNumber(input) -> true

            else -> false
        }
    }


    /**
     * Validates password using the reusable PasswordValidator with sign-in specific rules.
     */
    private fun validatePassword(
        value: String,
        stringProvider: StringResourceProvider
    ): FieldValidationResult {
        return PasswordValidator.validate(value, PasswordValidator.Rules.SIGNIN, stringProvider)
    }

    // Validate entire form at once (useful for submission)
    fun validateForm(stringProvider: StringResourceProvider): SignInForm {
        return copy(
            identifier = identifier.copy(
                validation = validateIdentifier(identifier.value, stringProvider),
                isTouched = true
            ),
            password = password.copy(
                validation = validatePassword(password.value, stringProvider),
                isTouched = true
            )
        )
    }

    // Reset form to initial state
    fun reset(): SignInForm {
        return SignInForm()
    }

    // Create a form with pre-filled identifier (useful for "remember me" functionality)
    fun withRememberedIdentifier(rememberedIdentifier: String): SignInForm {
        return copy(
            identifier = ValidatedField(rememberedIdentifier),
            rememberMe = true
        )
    }

    /**
     * Get the detected type of the current identifier with smart detection.
     */
    val identifierType: IdentifierType
        get() = detectIdentifierType(identifier.value)

    companion object {
        /**
         * Create a form and auto-detect the identifier type for better UX.
         *
         * @param identifier The identifier (email, phone, or username)
         * @return SignInForm with properly formatted identifier
         */
        fun withAutoDetectedIdentifier(identifier: String): SignInForm {
            val cleanIdentifier = identifier.trim()
            val form = SignInForm().withRememberedIdentifier(cleanIdentifier)

            // If it's detected as a phone number, try to format it
            if (form.identifierType == IdentifierType.PHONE) {
                val formatted = PhoneNumberValidator.formatMobile(
                    cleanIdentifier,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
                )
                if (formatted != null) {
                    return SignInForm().withRememberedIdentifier(formatted)
                }
            }

            return form
        }
    }

    /**
     * Enum representing different types of identifiers.
     */
    enum class IdentifierType {
        EMAIL,
        PHONE,
        USERNAME
    }
}