package com.amadiyawa.feature_base.domain.model

import com.droidkotlin.core.R
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import timber.log.Timber

/**
 * Professional utility for comprehensive mobile phone number validation.
 *
 * This utility leverages Google's libphonenumber library to provide robust
 * mobile phone validation with international support and locale-aware error messages.
 *
 * Features:
 * - International mobile phone number validation
 * - Multiple region support with comprehensive fallback
 * - Mobile vs landline type detection
 * - International formatting (E.164, National, International)
 * - Locale-aware error messages (English/French)
 * - Smart phone number pattern detection
 * - Input cleaning and normalization
 * - Comprehensive error handling and logging
 *
 * @author Amadou Iyawa
 */
object PhoneNumberValidator {

    // Default region (can be any region - CM used as sensible default)
    private const val DEFAULT_REGION = "CM"

    // Comprehensive fallback regions for international support
    private val FALLBACK_REGIONS = listOf(
        "JP", "NG", "US", "CA", "GB", "FR", "DE", "IT",
        "ES", "AU", "RW", "CN", "IN", "BR", "RU", "MX", "ZA"
    )

    private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }

    /**
     * Mobile phone validation result with comprehensive information.
     */
    data class ValidationResult(
        val isValid: Boolean,
        val isMobile: Boolean = false,
        val formattedNumber: String? = null,
        val internationalFormat: String? = null,
        val nationalFormat: String? = null,
        val e164Format: String? = null,
        val countryCode: Int? = null,
        val regionCode: String? = null,
        val numberType: PhoneNumberType? = null,
        val errorReason: String? = null,
        val originalInput: String? = null
    ) {
        val isLandline: Boolean
            get() = numberType == PhoneNumberType.FIXED_LINE || numberType == PhoneNumberType.FIXED_LINE_OR_MOBILE

        val isPossible: Boolean
            get() = isValid || numberType != null
    }

    /**
     * Comprehensive mobile phone validation with detailed result.
     *
     * @param input The phone number to validate
     * @param region The primary region code for validation (defaults to CM)
     * @param stringProvider Provider for localized error messages
     * @return ValidationResult with comprehensive mobile validation information
     */
    fun validateMobile(
        input: String?,
        region: String = DEFAULT_REGION,
        stringProvider: StringResourceProvider
    ): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.phone_number_required),
                originalInput = input
            )
        }

        val cleanInput = input.trim()

        // Try parsing with primary region first
        val phoneNumber = tryParseNumber(cleanInput, region)
        if (phoneNumber != null && phoneUtil.isValidNumber(phoneNumber)) {
            return createValidationResult(phoneNumber, cleanInput, stringProvider)
        }

        // Try fallback regions for international support
        for (fallbackRegion in FALLBACK_REGIONS) {
            if (fallbackRegion != region) {
                val fallbackNumber = tryParseNumber(cleanInput, fallbackRegion)
                if (fallbackNumber != null && phoneUtil.isValidNumber(fallbackNumber)) {
                    return createValidationResult(fallbackNumber, cleanInput, stringProvider)
                }
            }
        }

        // All parsing attempts failed
        return ValidationResult(
            isValid = false,
            errorReason = getLocalizedErrorReason(cleanInput, region, stringProvider),
            originalInput = cleanInput
        )
    }

    /**
     * Quick mobile validation check with locale support.
     *
     * @param input The phone number to validate
     * @param region The region code for validation (defaults to CM)
     * @param stringProvider Provider for localized error messages
     * @return true if the number is a valid mobile number
     */
    fun isMobileValid(
        input: String?,
        region: String = DEFAULT_REGION,
        stringProvider: StringResourceProvider
    ): Boolean {
        val result = validateMobile(input, region, stringProvider)
        return result.isValid && result.isMobile
    }

    /**
     * Format mobile number for display in specified format.
     *
     * @param input The phone number to format
     * @param format The desired format (defaults to INTERNATIONAL)
     * @param region The region code for parsing (defaults to CM)
     * @return Formatted phone number or null if invalid/not mobile
     */
    fun formatMobile(
        input: String?,
        format: PhoneNumberUtil.PhoneNumberFormat = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL,
        region: String = DEFAULT_REGION
    ): String? {
        if (input.isNullOrBlank()) return null

        val phoneNumber = tryParseNumber(input.trim(), region) ?: run {
            // Try fallback regions
            FALLBACK_REGIONS.firstNotNullOfOrNull { fallbackRegion ->
                tryParseNumber(input.trim(), fallbackRegion)
            }
        } ?: return null

        if (!phoneUtil.isValidNumber(phoneNumber) || !isMobileType(phoneNumber)) {
            return null
        }

        return try {
            phoneUtil.format(phoneNumber, format)
        } catch (e: Exception) {
            Timber.w(e, "Failed to format mobile number: $input")
            null
        }
    }

    /**
     * Get the region code for a phone number.
     *
     * @param input The phone number
     * @param region The region code for parsing (defaults to CM)
     * @return The ISO country code or null if invalid
     */
    fun getRegionCode(input: String?, region: String = DEFAULT_REGION): String? {
        if (input.isNullOrBlank()) return null

        val phoneNumber = tryParseNumber(input.trim(), region) ?: run {
            FALLBACK_REGIONS.firstNotNullOfOrNull { fallbackRegion ->
                tryParseNumber(input.trim(), fallbackRegion)
            }
        } ?: return null

        return phoneUtil.getRegionCodeForNumber(phoneNumber)
    }

    // Private helper methods

    private fun tryParseNumber(input: String, region: String): PhoneNumber? {
        return try {
            phoneUtil.parse(input, region)
        } catch (e: NumberParseException) {
            Timber.d("Failed to parse phone number '$input' for region '$region': ${e.errorType}")
            null
        } catch (e: Exception) {
            Timber.w(e, "Unexpected error parsing phone number: $input")
            null
        }
    }

    private fun isMobileType(phoneNumber: PhoneNumber): Boolean {
        val numberType = phoneUtil.getNumberType(phoneNumber)
        return numberType == PhoneNumberType.MOBILE || numberType == PhoneNumberType.FIXED_LINE_OR_MOBILE
    }

    private fun createValidationResult(
        phoneNumber: PhoneNumber,
        originalInput: String,
        stringProvider: StringResourceProvider
    ): ValidationResult {
        return try {
            val isMobile = isMobileType(phoneNumber)

            ValidationResult(
                isValid = true,
                isMobile = isMobile,
                formattedNumber = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL),
                internationalFormat = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL),
                nationalFormat = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL),
                e164Format = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164),
                countryCode = phoneNumber.countryCode,
                regionCode = phoneUtil.getRegionCodeForNumber(phoneNumber),
                numberType = phoneUtil.getNumberType(phoneNumber),
                originalInput = originalInput
            )
        } catch (e: Exception) {
            Timber.w(e, "Error creating validation result for: $originalInput")
            ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.phone_processing_error),
                originalInput = originalInput
            )
        }
    }

    private fun getLocalizedErrorReason(
        input: String,
        region: String,
        stringProvider: StringResourceProvider
    ): String {
        return try {
            phoneUtil.parse(input, region)
            stringProvider.getString(R.string.phone_format_not_recognized)
        } catch (e: NumberParseException) {
            when (e.errorType) {
                NumberParseException.ErrorType.INVALID_COUNTRY_CODE ->
                    stringProvider.getString(R.string.phone_invalid_country_code)
                NumberParseException.ErrorType.NOT_A_NUMBER ->
                    stringProvider.getString(R.string.phone_not_valid_number)
                NumberParseException.ErrorType.TOO_SHORT_NSN ->
                    stringProvider.getString(R.string.phone_too_short)
                NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD ->
                    stringProvider.getString(R.string.phone_too_short_after_prefix)
                NumberParseException.ErrorType.TOO_LONG ->
                    stringProvider.getString(R.string.phone_too_long)
                else -> stringProvider.getString(R.string.phone_invalid_format)
            }
        } catch (_: Exception) {
            stringProvider.getString(R.string.phone_validation_error)
        }
    }

    /**
     * Utility methods for phone number operations.
     */
    object Utils {
        /**
         * Check if input looks like a phone number (smart pattern detection).
         * Uses multiple heuristics optimized for international mobile detection.
         */
        fun looksLikePhoneNumber(input: String?): Boolean {
            if (input.isNullOrBlank()) return false

            val digits = input.replace(Regex("[^0-9]"), "")

            return when {
                // Too short to be a phone number (avoid false positives)
                digits.length < 7 -> false

                // International format with + (most reliable)
                input.startsWith("+") && digits.length >= 8 -> true

                // Local format starting with 0 (common internationally)
                input.startsWith("0") && digits.length >= 9 -> true

                // Contains typical phone formatting
                input.matches(Regex(".*[\\(\\)\\-\\s].*")) && digits.length >= 8 -> true

                // Long string of digits (10+ very likely phone)
                digits.length >= 10 -> true

                // Common mobile patterns (region-agnostic)
                isCommonMobilePattern(input) -> true

                else -> false
            }
        }

        /**
         * Check against common mobile phone patterns across regions.
         */
        fun isCommonMobilePattern(input: String): Boolean {
            val patterns = listOf(
                // International formats
                Regex("^\\+[1-9]\\d{7,14}$"),
                Regex("^\\+[1-9]\\d{0,3}[\\s\\-]\\d{7,11}$"),

                // Local formats with leading mobile indicators
                Regex("^[6-9]\\d{8}$"),              // Many countries use 6-9 for mobile
                Regex("^0[6-9]\\d{8}$"),            // With leading 0

                // Formatted patterns
                Regex("^\\([0-9]{3}\\)[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{4}$"), // (123) 456-7890
                Regex("^[0-9]{3}[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{4}$"),       // 123-456-7890
                Regex("^[0-9]{2}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$") // International mobile
            )

            return patterns.any { it.matches(input.trim()) }
        }

        /**
         * Clean phone number input by removing common formatting characters.
         */
        fun cleanInput(input: String?): String? {
            if (input.isNullOrBlank()) return input
            return input.replace(Regex("[\\s\\-\\(\\)\\.]+"), "")
        }

        /**
         * Extract numeric part of phone number (preserving + for international).
         */
        fun extractNumbers(input: String?): String? {
            if (input.isNullOrBlank()) return null
            return input.replace(Regex("[^0-9+]"), "")
        }

        /**
         * Normalize phone number for comparison (remove formatting, lowercase).
         */
        fun normalizeInput(input: String?): String? {
            if (input.isNullOrBlank()) return null
            return input.trim().lowercase().replace(Regex("[\\s\\-\\(\\)\\.]+"), "")
        }
    }
}