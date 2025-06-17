package com.amadiyawa.feature_base.domain.error

/**
 * Represents different types of errors that can occur in the application
 */
sealed class DomainError {
    // Network Errors
    data class NetworkError(
        val type: NetworkErrorType,
        val code: Int? = null,
        val message: String? = null
    ) : DomainError()

    // Authentication Errors
    data class AuthenticationError(
        val type: AuthErrorType,
        val code: Int? = null,
        val message: String? = null
    ) : DomainError()

    // Validation Errors
    data class ValidationError(
        val field: String,
        val type: ValidationErrorType,
        val message: String? = null
    ) : DomainError()

    // Business Logic Errors
    data class BusinessError(
        val type: BusinessErrorType,
        val code: Int? = null,
        val message: String? = null,
        val metadata: Map<String, Any> = emptyMap()
    ) : DomainError()

    // System Errors
    data class SystemError(
        val throwable: Throwable,
        val message: String? = null
    ) : DomainError()

    // Unknown/Generic Errors
    data class UnknownError(
        val code: Int? = null,
        val message: String? = null,
        val throwable: Throwable? = null
    ) : DomainError()
}

enum class NetworkErrorType {
    NO_CONNECTION,
    TIMEOUT,
    HOST_UNREACHABLE,
    SSL_ERROR,
    SLOW_CONNECTION
}

enum class AuthErrorType {
    INVALID_CREDENTIALS,
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    ACCOUNT_LOCKED,
    ACCOUNT_NOT_VERIFIED,
    SESSION_EXPIRED,
    INSUFFICIENT_PERMISSIONS
}

enum class ValidationErrorType {
    REQUIRED_FIELD_MISSING,
    INVALID_EMAIL_FORMAT,
    INVALID_PHONE_FORMAT,
    PASSWORD_TOO_WEAK,
    INVALID_LENGTH,
    INVALID_FORMAT
}

enum class BusinessErrorType {
    RESOURCE_NOT_FOUND,
    RESOURCE_ALREADY_EXISTS,
    OPERATION_NOT_ALLOWED,
    QUOTA_EXCEEDED,
    SERVICE_UNAVAILABLE,
    EXTERNAL_SERVICE_ERROR
}
