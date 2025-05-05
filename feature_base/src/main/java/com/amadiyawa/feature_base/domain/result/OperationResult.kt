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
     * Represents a known failure during an operation, typically related to business rules.
     *
     * This class encapsulates predictable errors that may occur during operation execution, such as:
     * - Validation errors
     * - Authentication issues
     * - Resource not found
     * - Business rule violations
     *
     * @property code Optional error code (e.g., HTTP status code, business error code)
     * @property message Optional user-readable error message
     * @property details Optional additional context (e.g., validation errors)
     */
    data class Failure(
        val code: Int? = null,
        val message: String? = null,
        val details: Any? = null
    ) : OperationResult<Nothing>

    /**
     * Represents an unexpected or technical error that occurred during an operation.
     *
     * This class is used to encapsulate unhandled exceptions and system-level errors such as:
     * - Network connectivity issues
     * - I/O exceptions
     * - Database errors
     * - API parsing failures
     * - Runtime crashes
     *
     * @property throwable The underlying exception that caused this error, if available
     * @property message A description of the error, defaults to the throwable's message
     */
    data class Error(
        val throwable: Throwable? = null,
        val code: Int? = null,
        val message: String? = throwable?.message
    ) : OperationResult<Nothing>

    companion object {
        fun <T> success(data: T): OperationResult<T> = Success(data)
        fun failure(code: Int? = null, message: String? = null, details: Any? = null): Failure =
            Failure(code, message, details)
        fun error(throwable: Throwable? = null,code: Int? = null, message: String? = null): Error =
            Error(throwable, code, message)
    }
}

/**
 * Maps the data of a successful [OperationResult] using the provided [transform] function.
 *
 * This function allows transforming the data contained in an [OperationResult.Success] while
 * preserving the failure or error context. If the result is a [Failure] or [Error],
 * it is returned unchanged.
 *
 * @param T The type of the original data
 * @param R The type of the transformed data
 * @param transform The transformation function to apply to the data in case of success
 * @return A new [OperationResult] containing either:
 *         - the transformed data if successful
 *         - the original failure or error otherwise
 */
inline fun <T, R> OperationResult<T>.map(transform: (T) -> R): OperationResult<R> =
    when (this) {
        is OperationResult.Success -> OperationResult.Success(transform(data))
        is OperationResult.Failure -> this
        is OperationResult.Error -> this
    }

inline fun <T, R> OperationResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (OperationResult.Failure) -> R,
    onError: (OperationResult.Error) -> R
): R {
    return when (this) {
        is OperationResult.Success -> onSuccess(this.data)
        is OperationResult.Failure -> onFailure(this)
        is OperationResult.Error -> onError(this)
    }
}