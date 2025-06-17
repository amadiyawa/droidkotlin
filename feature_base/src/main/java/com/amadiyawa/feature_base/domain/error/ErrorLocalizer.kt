package com.amadiyawa.feature_base.domain.error

/**
 * Interface for localizing error messages, titles, and action texts.
 *
 * This interface provides methods to retrieve localized strings for various
 * error-related elements, such as the error message, title, and action text.
 * Implementations of this interface should define how localization is handled
 * for different types of domain errors.
 */
interface ErrorLocalizer {
    /**
     * Retrieves the localized message for a given domain error.
     *
     * @param error The domain error for which the localized message is required.
     * @return The localized error message as a string.
     */
    fun getLocalizedMessage(error: DomainError): String

    /**
     * Retrieves the localized title for a given domain error.
     *
     * @param error The domain error for which the localized title is required.
     * @return The localized error title as a string, or null if no title is available.
     */
    fun getLocalizedTitle(error: DomainError): String?

    /**
     * Retrieves the localized action text for a given domain error.
     *
     * @param error The domain error for which the localized action text is required.
     * @return The localized action text as a string, or null if no action text is available.
     */
    fun getLocalizedActionText(error: DomainError): String?
}