package com.amadiyawa.feature_base.domain.result

/**
 * Transforms the success value of a [OperationResult] instance using the provided [onOperationResult] function.
 *
 * This is an inline function, which means the compiler will replace calls to this function with its body,
 * potentially improving performance. It takes a single parameter, [onOperationResult], which is a lambda function.
 * This lambda function is an extension function on [OperationResult.Success], meaning it can be called on instances
 * of [OperationResult.Success] as if it were a method of that class.
 *
 * @param T The type of the success value.
 * @param onOperationResult A lambda function that transforms the success value.
 * @return A new [OperationResult] instance with the transformed success value, or the original [OperationResult] instance
 * if it was a [OperationResult.Failure].
 */
inline fun <T> OperationResult<T>.mapSuccess(
    crossinline onOperationResult: OperationResult.Success<T>.() -> OperationResult<T>,
): OperationResult<T> {
    // Check if the Result instance is a Success
    if (this is OperationResult.Success) {
        // If it is, apply the transformation function to the success value
        return onOperationResult(this)
    }
    // If the Result instance is not a Success (i.e., it's a Failure), return it unmodified
    return this
}