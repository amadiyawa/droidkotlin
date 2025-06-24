package com.amadiyawa.feature_base.domain.model
import com.droidkotlin.core.R
import com.amadiyawa.feature_base.common.resources.StringResourceProvider
import timber.log.Timber

/**
 * Professional utility for comprehensive username validation.
 *
 * This utility provides robust username validation following industry standards
 * and best practices with locale-aware error messages for international applications.
 *
 * Features:
 * - Industry-standard username validation rules
 * - Multiple validation profiles (conservative, standard, liberal)
 * - Reserved word detection and filtering
 * - Username strength scoring and analysis
 * - Smart suggestion generation for invalid usernames
 * - Locale-aware error messages (English/French)
 * - Pattern-based validation with comprehensive checks
 * - Comprehensive error handling and logging
 *
 * @author Amadou Iyawa
 */
object UsernameValidator {

    // Username validation constants following industry standards
    private const val MIN_LENGTH_CONSERVATIVE = 3
    private const val MAX_LENGTH_CONSERVATIVE = 16
    private const val MIN_LENGTH_STANDARD = 3
    private const val MAX_LENGTH_STANDARD = 30
    private const val MIN_LENGTH_LIBERAL = 2
    private const val MAX_LENGTH_LIBERAL = 50

    // Reserved usernames (system, admin, common terms)
    private val RESERVED_USERNAMES = setOf(
        // System reserved
        "admin", "administrator", "root", "system", "user", "guest", "anonymous",
        "api", "www", "mail", "email", "support", "help", "info", "contact",
        "service", "services", "account", "accounts", "security", "privacy",

        // Common reserved
        "null", "undefined", "true", "false", "test", "demo", "example",
        "default", "config", "settings", "profile", "dashboard", "home",

        // Platform specific
        "moderator", "mod", "staff", "team", "official", "verified",
        "bot", "robot", "automated", "notification", "notifications"
    )

    // Basic inappropriate content list (expandable based on content policy)
    private val INAPPROPRIATE_WORDS = setOf(
        "spam", "fake", "scam", "hack", "hacker", "virus", "malware",
        "abuse", "fraud", "phishing", "exploit"
    )

    /**
     * Username validation profiles for different use cases.
     */
    enum class ValidationProfile {
        CONSERVATIVE,  // Banks, corporate systems (strict rules)
        STANDARD,      // Most applications (balanced approach)
        LIBERAL        // Social media, creative platforms (flexible)
    }

    /**
     * Username validation result with comprehensive information.
     */
    data class ValidationResult(
        val isValid: Boolean,
        val normalizedUsername: String? = null,
        val isReserved: Boolean = false,
        val isInappropriate: Boolean = false,
        val hasConsecutiveSpecialChars: Boolean = false,
        val startsWithLetter: Boolean = false,
        val endsWithSpecialChar: Boolean = false,
        val strengthScore: Int = 0,
        val errorReason: String? = null,
        val suggestions: List<String> = emptyList(),
        val originalInput: String? = null
    ) {
        val isAvailable: Boolean
            get() = isValid && !isReserved && !isInappropriate

        val isStrong: Boolean
            get() = strengthScore >= 80

        val isWeak: Boolean
            get() = strengthScore < 40
    }

    /**
     * Comprehensive username validation with detailed result.
     *
     * @param input The username to validate
     * @param profile The validation profile to use (defaults to STANDARD)
     * @param stringProvider Provider for localized error messages
     * @return ValidationResult with comprehensive validation information
     */
    fun validateDetailed(
        input: String?,
        profile: ValidationProfile = ValidationProfile.STANDARD,
        stringProvider: StringResourceProvider
    ): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_required),
                originalInput = input
            )
        }

        val cleanInput = input.trim()
        val normalizedUsername = normalizeUsername(cleanInput)

        // Get profile-specific rules
        val rules = getValidationRules(profile)

        // Length validation
        if (cleanInput.length < rules.minLength) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_too_short, rules.minLength),
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        if (cleanInput.length > rules.maxLength) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_too_long, rules.maxLength),
                originalInput = input
            )
        }

        // Character validation
        if (!cleanInput.matches(rules.allowedCharsRegex)) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_invalid_characters),
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // Pattern-specific validations
        val startsWithLetter = cleanInput.first().isLetter()
        val endsWithSpecialChar = cleanInput.last() in "._-"
        val hasConsecutiveSpecialChars = cleanInput.contains(Regex("[._-]{2,}"))

        // Must start with letter validation (for conservative and standard profiles)
        if (rules.mustStartWithLetter && !startsWithLetter) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_must_start_with_letter),
                startsWithLetter = false,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // Cannot end with special characters
        if (rules.cannotEndWithSpecialChar && endsWithSpecialChar) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_cannot_end_with_special),
                endsWithSpecialChar = true,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // No consecutive special characters
        if (rules.noConsecutiveSpecialChars && hasConsecutiveSpecialChars) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_no_consecutive_special),
                hasConsecutiveSpecialChars = true,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // Reserved username check
        val isReserved = RESERVED_USERNAMES.contains(normalizedUsername.lowercase())
        if (isReserved) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_reserved),
                isReserved = true,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // Inappropriate content check
        val isInappropriate = containsInappropriateContent(normalizedUsername)
        if (isInappropriate) {
            return ValidationResult(
                isValid = false,
                errorReason = stringProvider.getString(R.string.username_inappropriate),
                isInappropriate = true,
                originalInput = input,
                suggestions = generateSuggestions(cleanInput, profile, stringProvider)
            )
        }

        // Calculate strength score
        val strengthScore = calculateStrengthScore(normalizedUsername, startsWithLetter, hasConsecutiveSpecialChars, endsWithSpecialChar)

        // All validations passed
        return ValidationResult(
            isValid = true,
            normalizedUsername = normalizedUsername,
            startsWithLetter = startsWithLetter,
            endsWithSpecialChar = endsWithSpecialChar,
            hasConsecutiveSpecialChars = hasConsecutiveSpecialChars,
            strengthScore = strengthScore,
            originalInput = input
        )
    }

    /**
     * Quick username validation check.
     *
     * @param input The username to validate
     * @param profile The validation profile to use (defaults to STANDARD)
     * @param stringProvider Provider for localized error messages
     * @return true if the username is valid, false otherwise
     */
    fun isValid(
        input: String?,
        profile: ValidationProfile = ValidationProfile.STANDARD,
        stringProvider: StringResourceProvider
    ): Boolean {
        return validateDetailed(input, profile, stringProvider).isValid
    }

    /**
     * Check if username is available (valid and not reserved).
     *
     * @param input The username to check
     * @param profile The validation profile to use (defaults to STANDARD)
     * @param stringProvider Provider for localized error messages
     * @return true if username is available for use
     */
    fun isAvailable(
        input: String?,
        profile: ValidationProfile = ValidationProfile.STANDARD,
        stringProvider: StringResourceProvider
    ): Boolean {
        val result = validateDetailed(input, profile, stringProvider)
        return result.isAvailable
    }

    /**
     * Normalize username for comparison and storage.
     *
     * @param input The username to normalize
     * @return Normalized username or null if input is invalid
     */
    fun normalize(input: String?): String? {
        if (input.isNullOrBlank()) return null
        return normalizeUsername(input.trim())
    }

    /**
     * Generate username suggestions based on input.
     *
     * @param input The original username input
     * @param profile The validation profile to use
     * @param stringProvider Provider for localized error messages
     * @return List of valid username suggestions
     */
    fun generateSuggestions(
        input: String?,
        profile: ValidationProfile = ValidationProfile.STANDARD,
        stringProvider: StringResourceProvider
    ): List<String> {
        if (input.isNullOrBlank()) return emptyList()

        val cleanInput = input.trim()
        val suggestions = mutableListOf<String>()

        try {
            // Fix common issues
            var base = cleanInput.lowercase()
                .replace(Regex("[^a-zA-Z0-9._-]"), "")  // Remove invalid chars
                .replace(Regex("[._-]{2,}"), "_")        // Fix consecutive special chars

            // Ensure starts with letter if required
            val rules = getValidationRules(profile)
            if (rules.mustStartWithLetter && base.isNotEmpty() && !base.first().isLetter()) {
                base = "user$base"
            }

            // Remove trailing special chars
            base = base.trimEnd('_', '.', '-')

            // Ensure minimum length
            if (base.length < rules.minLength) {
                base = base.padEnd(rules.minLength, '0')
            }

            // Ensure maximum length
            if (base.length > rules.maxLength) {
                base = base.take(rules.maxLength - 3) // Leave room for suffixes
            }

            // Generate variations if base is valid
            if (base.isNotEmpty()) {
                suggestions.add(base)
                suggestions.add("${base}123")
                suggestions.add("${base}_1")
                suggestions.add("${base}.user")

                // Add year variations
                val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                suggestions.add("$base$currentYear")
                suggestions.add("${base}_${currentYear.toString().takeLast(2)}")
            }

        } catch (e: Exception) {
            Timber.w(e, "Error generating username suggestions for: $input")
        }

        // Filter and validate suggestions
        return suggestions
            .distinct()
            .filter { isValid(it, profile, stringProvider) }
            .take(5)
    }

    // Private methods

    private data class ValidationRules(
        val minLength: Int,
        val maxLength: Int,
        val allowedCharsRegex: Regex,
        val mustStartWithLetter: Boolean,
        val cannotEndWithSpecialChar: Boolean,
        val noConsecutiveSpecialChars: Boolean
    )

    private fun getValidationRules(profile: ValidationProfile): ValidationRules {
        return when (profile) {
            ValidationProfile.CONSERVATIVE -> ValidationRules(
                minLength = MIN_LENGTH_CONSERVATIVE,
                maxLength = MAX_LENGTH_CONSERVATIVE,
                allowedCharsRegex = Regex("^[a-zA-Z][a-zA-Z0-9._-]{2,15}$"),
                mustStartWithLetter = true,
                cannotEndWithSpecialChar = true,
                noConsecutiveSpecialChars = true
            )

            ValidationProfile.STANDARD -> ValidationRules(
                minLength = MIN_LENGTH_STANDARD,
                maxLength = MAX_LENGTH_STANDARD,
                allowedCharsRegex = Regex("^[a-zA-Z][a-zA-Z0-9._-]{2,29}$"),
                mustStartWithLetter = true,
                cannotEndWithSpecialChar = true,
                noConsecutiveSpecialChars = true
            )

            ValidationProfile.LIBERAL -> ValidationRules(
                minLength = MIN_LENGTH_LIBERAL,
                maxLength = MAX_LENGTH_LIBERAL,
                allowedCharsRegex = Regex("^[a-zA-Z0-9._-]{2,50}$"),
                mustStartWithLetter = false,
                cannotEndWithSpecialChar = false,
                noConsecutiveSpecialChars = false
            )
        }
    }

    private fun normalizeUsername(input: String): String {
        return input.trim()
    }

    private fun containsInappropriateContent(username: String): Boolean {
        val lowerUsername = username.lowercase()
        return INAPPROPRIATE_WORDS.any { lowerUsername.contains(it) }
    }

    private fun calculateStrengthScore(
        username: String,
        startsWithLetter: Boolean,
        hasConsecutiveSpecialChars: Boolean,
        endsWithSpecialChar: Boolean
    ): Int {
        var score = 100

        // Deduct points for various issues
        if (!startsWithLetter) score -= 20
        if (hasConsecutiveSpecialChars) score -= 15
        if (endsWithSpecialChar) score -= 10
        if (username.length < 5) score -= 20
        if (!username.any { it.isDigit() }) score -= 10
        if (!username.any { it in "._-" }) score -= 5

        return score.coerceIn(0, 100)
    }

    /**
     * Utility methods for username operations.
     */
    object Utils {
        /**
         * Check if input looks like a username (quick pattern check).
         */
        fun looksLikeUsername(input: String?): Boolean {
            if (input.isNullOrBlank()) return false
            return input.matches(Regex("^[a-zA-Z0-9._-]{2,50}$")) &&
                    !input.contains("@") && !input.contains(" ")
        }

        /**
         * Extract base username from decorated username (remove numbers/decorations).
         */
        fun extractBaseUsername(username: String?): String? {
            if (username.isNullOrBlank()) return null
            return username.replace(Regex("[0-9._-]+$"), "")
        }

        /**
         * Check username strength score (0-100).
         */
        fun getStrengthScore(
            username: String?,
            stringProvider: StringResourceProvider
        ): Int {
            val result = validateDetailed(username, stringProvider = stringProvider)
            return result.strengthScore
        }

        /**
         * Check if username is likely reserved.
         */
        fun isLikelyReserved(username: String?): Boolean {
            if (username.isNullOrBlank()) return false
            return RESERVED_USERNAMES.contains(username.lowercase())
        }

        /**
         * Clean username input by removing invalid characters.
         */
        fun cleanInput(input: String?): String? {
            if (input.isNullOrBlank()) return input
            return input.replace(Regex("[^a-zA-Z0-9._-]"), "")
        }
    }
}