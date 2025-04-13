package com.amadiyawa.feature_base.common.util

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException
import timber.log.Timber

/**
 * Utility object for validating phone numbers.
 *
 * This object leverages the Google libphonenumber library to parse and validate
 * phone numbers. It provides methods to ensure that a given phone number is valid
 * for a specific region.
 */
object PhoneNumberValidator {

    private const val DEFAULT_REGION = "CM"

    /**
     *
     * @param input The phone number to validate. Can be null or blank.
     * @param region The region code to use for validation. Defaults to "CM" (Cameroon).
     * @return `true` if the phone number is valid, otherwise `false`.
     *
     * This function uses the Google libphonenumber library to parse and validate
     * the phone number. It handles null or blank inputs gracefully and logs any
     * parsing errors using Timber.
     */
    fun isValid(input: String?, region: String = DEFAULT_REGION): Boolean {
        if (input.isNullOrBlank()) {
            Timber.w("Empty or null phone number.")
            return false
        }

        return try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val number = phoneUtil.parse(input, region)
            phoneUtil.isValidNumber(number)
        } catch (e: NumberParseException) {
            Timber.e(e, "Error when analyzing the phone number: $input")
            false
        }
    }
}