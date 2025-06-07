package com.amadiyawa.feature_base.domain.model

import com.amadiyawa.droidkotlin.base.R
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import timber.log.Timber
import java.net.IDN
import java.util.regex.Pattern

/**
 * Professional utility for comprehensive email validation.
 *
 * This utility provides robust email validation following RFC 5322 standards
 * with locale-aware error messages and practical checks for real-world usage.
 *
 * Features:
 * - RFC 5322 compliant email validation with practical adaptations
 * - International domain name support (IDN) for global usage
 * - Disposable email provider detection and filtering
 * - Corporate vs personal email classification
 * - Email normalization and formatting utilities
 * - Smart typo detection and correction suggestions
 * - Locale-aware error messages (English/French)
 * - Comprehensive security and length validations
 * - Email masking for privacy and display purposes
 *
 * @author Amadou Iyawa
 */
object EmailValidator {

    // Email validation constants following RFC standards
    private const val MAX_EMAIL_LENGTH = 254        // RFC 5321 limit
    private const val MAX_LOCAL_PART_LENGTH = 64    // RFC 5321 limit
    private const val MAX_DOMAIN_LENGTH = 253       // RFC 1035 limit

    // Common disposable email domains (expandable list)
    private val DISPOSABLE_DOMAINS = setOf(
        "10minutemail.com", "guerrillamail.com", "mailinator.com",
        "tempmail.org", "throwaway.email", "temp-mail.org",
        "getnada.com", "maildrop.cc", "trashmail.com", "sharklasers.com",
        "yopmail.com", "emailondeck.com", "tempmail.ninja"
    )

    // Corporate/personal email domains (major providers)
    private val PERSONAL_EMAIL_DOMAINS = setOf(
        "gmail.com", "yahoo.com", "hotmail.com", "outlook.com",
        "icloud.com", "aol.com", "protonmail.com", "mail.com",
        "live.com", "msn.com", "ymail.com", "rocketmail.com"
    )

    // RFC 5322 compliant email regex (practical version)
    private val EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )

    // Stricter pattern for high-security applications
    private val STRICT_EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9][a-zA-Z0-9._%+-]{0,63}@[a-zA-Z0-9][a-zA-Z0-9.-]{0,62}\\.[a-zA-Z]{2,63}$"
    )

    /**
     * Email validation result with comprehensive information.
     */
    data class ValidationResult(
        val isValid: Boolean,
        val normalizedEmail: String? = null,
        val localPart: String? = null,
        val domain: String? = null,
        val isDisposable: Boolean = false,
        val isPersonal: Boolean = false,
        val isCorporate: Boolean = false,
        val isInternational: Boolean = false,
        val strengthScore: Int = 0,
        val errorReason: String? = null,
        val suggestions: List<String> = emptyList(),
        val originalInput: String? = null
    ) {
        val isProfessional: Boolean
            get() = isValid && !isDisposable && !isPersonal

        val isReliable: Boolean
            get() = isValid && !isDisposable

        val isSecure: Boolean
            get() = strengthScore >= 80
    }

    /**
     * Comprehensive email validation with detailed result.
     *
     * @param input The email address to validate
     * @param allowInternational Whether to allow international domain names (defaults to true)
     * @param strict Whether to use strict validation rules (defaults to false)
     * @param stringProvider Provider for localized error messages
     * @return ValidationResult with comprehensive validation information
     */
    fun validateDetailed(
        input: String?,
        allowInternational: Boolean = true,
        strict: Boolean = false,
        stringProvider: StringResourceProvider
    ): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.email_required),
                originalInput = input
            )
        }

        val cleanInput = input.trim().lowercase()

        // Basic length check
        if (cleanInput.length > MAX_EMAIL_LENGTH) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.email_too_long, MAX_EMAIL_LENGTH),
                originalInput = input
            )
        }

        // Must contain exactly one @
        val atCount = cleanInput.count { it == '@' }
        if (atCount != 1) {
            return ValidationResult(
                isValid = false,
                errorReason = if (atCount == 0) {
                    stringProvider.getString(R.string.email_missing_at_symbol)
                } else {
                    stringProvider.getString(R.string.email_multiple_at_symbols)
                },
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, stringProvider)
            )
        }

        val parts = cleanInput.split("@")
        val localPart = parts[0]
        val domain = parts[1]

        // Validate local part
        val localValidation = validateLocalPart(localPart, stringProvider)
        if (!localValidation.first) {
            return ValidationResult(
                isValid = false,
                errorReason = localValidation.second,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, stringProvider)
            )
        }

        // Validate domain
        val domainValidation = validateDomain(domain, allowInternational, stringProvider)
        if (!domainValidation.first) {
            return ValidationResult(
                isValid = false,
                errorReason = domainValidation.second,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, stringProvider)
            )
        }

        // Pattern validation
        val pattern = if (strict) STRICT_EMAIL_PATTERN else EMAIL_PATTERN
        if (!pattern.matcher(cleanInput).matches()) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.email_invalid_format),
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, stringProvider)
            )
        }

        // Create successful result with classification
        val isDisposable = DISPOSABLE_DOMAINS.contains(domain)
        val isPersonal = PERSONAL_EMAIL_DOMAINS.contains(domain)
        val isCorporate = !isPersonal && !isDisposable && isBusinessDomain(domain)
        val isInternational = domain.contains("xn--") || !domain.all { it.code < 128 }
        val strengthScore = calculateStrengthScore(cleanInput, localPart, domain, isDisposable, isPersonal)

        return ValidationResult(
            isValid = true,
            normalizedEmail = cleanInput,
            localPart = localPart,
            domain = domain,
            isDisposable = isDisposable,
            isPersonal = isPersonal,
            isCorporate = isCorporate,
            isInternational = isInternational,
            strengthScore = strengthScore,
            originalInput = input
        )
    }

    /**
     * Quick email validation check.
     *
     * @param input The email address to validate
     * @param strict Whether to use strict validation (defaults to false)
     * @param stringProvider Provider for localized error messages
     * @return true if the email is valid, false otherwise
     */
    fun isValid(
        input: String?,
        strict: Boolean = false,
        stringProvider: StringResourceProvider
    ): Boolean {
        return validateDetailed(input, strict = strict, stringProvider = stringProvider).isValid
    }

    /**
     * Check if email is from a disposable email provider.
     *
     * @param input The email address to check
     * @param stringProvider Provider for localized error messages
     * @return true if email is from disposable provider
     */
    fun isDisposable(input: String?, stringProvider: StringResourceProvider): Boolean {
        val result = validateDetailed(input, stringProvider = stringProvider)
        return result.isValid && result.isDisposable
    }

    /**
     * Normalize email address for storage and comparison.
     *
     * @param input The email address to normalize
     * @return Normalized email address or null if invalid
     */
    fun normalize(input: String?): String? {
        if (input.isNullOrBlank()) return null
        return input.trim().lowercase()
    }

    /**
     * Extract domain from email address.
     *
     * @param input The email address
     * @return Domain part or null if invalid
     */
    fun extractDomain(input: String?): String? {
        if (input.isNullOrBlank()) return null
        val atIndex = input.indexOf("@")
        return if (atIndex > 0 && atIndex < input.length - 1) {
            input.substring(atIndex + 1).lowercase()
        } else null
    }

    /**
     * Generate email suggestions for common typos and errors.
     *
     * @param input The original email input
     * @param stringProvider Provider for localized error messages
     * @return List of suggested email corrections
     */
    fun generateSuggestions(input: String?, stringProvider: StringResourceProvider): List<String> {
        if (input.isNullOrBlank()) return emptyList()

        val suggestions = mutableListOf<String>()
        val cleanInput = input.trim().lowercase()

        try {
            if (cleanInput.contains("@")) {
                val parts = cleanInput.split("@")
                val localPart = parts[0]
                val domain = parts.getOrNull(1) ?: ""

                // Common domain typos
                val domainSuggestions = mapOf(
                    "gmai.com" to "gmail.com",
                    "gmial.com" to "gmail.com",
                    "gmail.co" to "gmail.com",
                    "gmail.cm" to "gmail.com",
                    "yahooo.com" to "yahoo.com",
                    "yahho.com" to "yahoo.com",
                    "yahoo.co" to "yahoo.com",
                    "hotmial.com" to "hotmail.com",
                    "hotmai.com" to "hotmail.com",
                    "outlok.com" to "outlook.com",
                    "outloo.com" to "outlook.com"
                )

                domainSuggestions[domain]?.let { correctDomain ->
                    suggestions.add("$localPart@$correctDomain")
                }

                // If no @ symbol, suggest adding domain
            } else if (cleanInput.isNotEmpty()) {
                suggestions.add("$cleanInput@gmail.com")
                suggestions.add("$cleanInput@yahoo.com")
                suggestions.add("$cleanInput@outlook.com")
            }
        } catch (e: Exception) {
            Timber.w(e, "Error generating email suggestions for: $input")
        }

        return suggestions.take(3)
    }

    // Private validation methods

    private fun validateLocalPart(localPart: String, stringProvider: StringResourceProvider): Pair<Boolean, String?> {
        return when {
            localPart.isEmpty() -> false to stringProvider.getString(R.string.email_local_empty)
            localPart.length > MAX_LOCAL_PART_LENGTH -> false to stringProvider.getString(R.string.email_local_too_long, MAX_LOCAL_PART_LENGTH)
            localPart.startsWith(".") -> false to stringProvider.getString(R.string.email_local_starts_with_dot)
            localPart.endsWith(".") -> false to stringProvider.getString(R.string.email_local_ends_with_dot)
            localPart.contains("..") -> false to stringProvider.getString(R.string.email_local_consecutive_dots)
            !localPart.matches(Regex("^[a-zA-Z0-9._%+-]+$")) -> false to stringProvider.getString(R.string.email_local_invalid_characters)
            else -> true to null
        }
    }

    private fun validateDomain(domain: String, allowInternational: Boolean, stringProvider: StringResourceProvider): Pair<Boolean, String?> {
        return when {
            domain.isEmpty() -> false to stringProvider.getString(R.string.email_domain_empty)
            domain.length > MAX_DOMAIN_LENGTH -> false to stringProvider.getString(R.string.email_domain_too_long, MAX_DOMAIN_LENGTH)
            domain.startsWith(".") -> false to stringProvider.getString(R.string.email_domain_starts_with_dot)
            domain.endsWith(".") -> false to stringProvider.getString(R.string.email_domain_ends_with_dot)
            domain.contains("..") -> false to stringProvider.getString(R.string.email_domain_consecutive_dots)
            !domain.contains(".") -> false to stringProvider.getString(R.string.email_domain_missing_dot)
            !isValidDomainFormat(domain, allowInternational) -> false to stringProvider.getString(R.string.email_domain_invalid_format)
            else -> true to null
        }
    }

    private fun isValidDomainFormat(domain: String, allowInternational: Boolean): Boolean {
        return try {
            if (allowInternational) {
                // Handle international domain names
                val asciiDomain = IDN.toASCII(domain)
                asciiDomain.matches(Regex("^[a-zA-Z0-9][a-zA-Z0-9.-]{0,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}$"))
            } else {
                domain.matches(Regex("^[a-zA-Z0-9][a-zA-Z0-9.-]{0,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}$"))
            }
        } catch (e: Exception) {
            Timber.w(e, "Error validating domain format: $domain")
            false
        }
    }

    private fun isBusinessDomain(domain: String): Boolean {
        // Simple heuristic: if it's not in personal domains and not disposable, likely business
        return !PERSONAL_EMAIL_DOMAINS.contains(domain) && !DISPOSABLE_DOMAINS.contains(domain)
    }

    private fun calculateStrengthScore(
        email: String,
        localPart: String,
        domain: String,
        isDisposable: Boolean,
        isPersonal: Boolean
    ): Int {
        var score = 100

        // Deduct points for various issues
        if (isDisposable) score -= 50
        if (localPart.length < 3) score -= 20
        if (localPart.length > 20) score -= 10
        if (!localPart.any { it.isLetter() }) score -= 15
        if (!localPart.any { it.isDigit() }) score -= 5
        if (isPersonal) score -= 10
        if (domain.split(".").size < 2) score -= 15

        return score.coerceIn(0, 100)
    }

    /**
     * Utility methods for email operations.
     */
    object Utils {
        /**
         * Extract username (local part) from email.
         */
        fun extractUsername(email: String?): String? {
            if (email.isNullOrBlank()) return null
            val atIndex = email.indexOf("@")
            return if (atIndex > 0) email.substring(0, atIndex) else null
        }

        /**
         * Check if email looks valid (quick pattern check).
         */
        fun looksLikeEmail(input: String?): Boolean {
            if (input.isNullOrBlank()) return false
            return input.contains("@") && input.contains(".") &&
                    input.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
        }

        /**
         * Mask email for display privacy (e.g., "j***@gmail.com").
         */
        fun maskEmail(email: String?): String? {
            if (email.isNullOrBlank()) return email

            val atIndex = email.indexOf("@")
            if (atIndex <= 0) return email

            val localPart = email.substring(0, atIndex)
            val domain = email.substring(atIndex)

            val maskedLocal = if (localPart.length <= 2) {
                localPart
            } else {
                localPart.first() + "*".repeat((localPart.length - 2).coerceAtLeast(1)) + localPart.last()
            }

            return maskedLocal + domain
        }

        /**
         * Check if domain is likely a business domain.
         */
        fun isBusinessDomain(email: String?): Boolean {
            val domain = extractDomain(email) ?: return false
            return !PERSONAL_EMAIL_DOMAINS.contains(domain) && !DISPOSABLE_DOMAINS.contains(domain)
        }

        /**
         * Get email strength score (0-100).
         */
        fun getStrengthScore(email: String?, stringProvider: StringResourceProvider): Int {
            val result = validateDetailed(email, stringProvider = stringProvider)
            return result.strengthScore
        }

        /**
         * Clean email input by removing extra whitespace.
         */
        fun cleanInput(input: String?): String? {
            if (input.isNullOrBlank()) return input
            return input.trim()
        }
    }
}