package com.amadiyawa.feature_base.domain.result

/**
 * A sealed interface that represents the result of an operation,
 * encapsulating success, business failure, or unexpected error states.
 *
 * @param T The type of data returned on success.
 *
 * - Use [Success] when the operation completes successfully and returns data.
 * - Use [Failure] when the operation completes with a known issue (e.g., invalid credentials).
 * - Use [Error] for unexpected exceptions (e.g., network failure, server crash).
 *
 * This pattern allows for clear, type-safe handling of all possible outcomes of an operation.
 *
 * @author Amadou Iyawa
 */
sealed interface OperationResult<out T> {

    /**
     * Represents a successful result containing a value.
     *
     * @param T The type of the value.
     * @property data The value of the successful result.
     */
    data class Success<T>(val data: T) : OperationResult<T>

    /**
     * Represents a known failure, typically due to a business logic issue.
     *
     * Example: invalid input, user not found, unauthorized access.
     *
     * @property code Optional error code (e.g., HTTP status code, business error code).
     * @property message Optional user-readable error message.
     */
    data class Failure(val code: Int? = null, val message: String? = null) : OperationResult<Nothing>

    /**
     * Represents an unexpected or technical error during the operation.
     *
     * Example: IOException, API parsing failure, crash.
     *
     * @property throwable The exception that was thrown, if available.
     */
    data class Error(val throwable: Throwable? = null) : OperationResult<Nothing>
}