package com.amadiyawa.feature_base.domain.validation

object ValidationPatterns {
    val FULL_NAME = Regex("^[A-Za-zÀ-ÿ'’\\- ]{2,50}$")
    val EMAIL = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,63}$")
    val USERNAME = Regex("^[a-zA-Z0-9._-]{3,20}$")
    const val PASSWORD_MIN_LENGTH = 8
}