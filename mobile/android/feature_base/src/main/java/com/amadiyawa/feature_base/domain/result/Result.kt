package com.amadiyawa.feature_base.domain.result

/**
 * A sealed interface representing the result of an operation, which can be either a success, failure, or exception.
 */
sealed interface Result<out T> {

    /**
     * Represents a successful result containing a value.
     *
     * @param T The type of the value.
     * @property value The value of the successful result.
     */
    data class Success<T>(val value: T) : Result<T>

    /**
     * Represents a failure result containing an optional error code and message.
     *
     * @property code The optional error code of the failure.
     * @property message The optional error message of the failure.
     */
    data class Failure(val code: Int? = null, val message: String? = null) : Result<Nothing>

    /**
     * Represents an exceptional result containing an optional throwable.
     *
     * @property throwable The optional throwable of the exception.
     */
    data class Exception(val throwable: Throwable? = null) : Result<Nothing>
}