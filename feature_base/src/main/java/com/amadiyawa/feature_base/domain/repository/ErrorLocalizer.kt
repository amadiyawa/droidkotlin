package com.amadiyawa.feature_base.domain.repository

/**
 * A functional interface responsible for localizing error messages in the application.
 *
 * This interface provides a mechanism to convert numeric error codes into human-readable,
 * localized messages. Being a functional interface, it can be implemented using lambda expressions.
 */
fun interface ErrorLocalizer {
    fun getLocalizedMessage(errorCode: Int, defaultMessage: String): String
}