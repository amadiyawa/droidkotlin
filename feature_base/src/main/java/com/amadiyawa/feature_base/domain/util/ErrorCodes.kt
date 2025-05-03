package com.amadiyawa.feature_base.domain.util

object ErrorCodes {
    // Authentication errors (100-199)
    const val INVALID_CREDENTIALS = 100
    const val SESSION_EXPIRED = 101
    const val UNAUTHORIZED_ACCESS = 102
    const val ACCOUNT_NOT_FOUND = 103
    const val TOO_MANY_ATTEMPTS = 104

    // Social auth errors (200-299)
    const val SOCIAL_ACCOUNT_NOT_LINKED = 200
    const val SOCIAL_AUTH_CANCELLED = 201
    const val SOCIAL_AUTH_CONNECTION_FAILED = 202

    // Session errors (300-399)
    const val SESSION_SAVE_FAILED = 300
    const val SESSION_ACTIVATION_FAILED = 301

    // Network errors (400-499)
    const val NO_INTERNET = 400
    const val CONNECTION_TIMEOUT = 401
    const val SERVER_UNAVAILABLE = 402
}