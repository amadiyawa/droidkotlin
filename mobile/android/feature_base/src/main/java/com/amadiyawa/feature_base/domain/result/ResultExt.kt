package com.amadiyawa.feature_base.domain.result

/**
 * Transforms the success value of a [Result] instance using the provided [onResult] function.
 *
 * This is an inline function, which means the compiler will replace calls to this function with its body,
 * potentially improving performance. It takes a single parameter, [onResult], which is a lambda function.
 * This lambda function is an extension function on [Result.Success], meaning it can be called on instances
 * of [Result.Success] as if it were a method of that class.
 *
 * @param T The type of the success value.
 * @param onResult A lambda function that transforms the success value.
 * @return A new [Result] instance with the transformed success value, or the original [Result] instance
 * if it was a [Result.Failure].
 */
inline fun <T> Result<T>.mapSuccess(
    crossinline onResult: Result.Success<T>.() -> Result<T>,
): Result<T> {
    // Check if the Result instance is a Success
    if (this is Result.Success) {
        // If it is, apply the transformation function to the success value
        return onResult(this)
    }
    // If the Result instance is not a Success (i.e., it's a Failure), return it unmodified
    return this
}