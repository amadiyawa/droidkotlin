package com.amadiyawa.feature_base.domain.validation

object ValidationPatterns {
    val CAMEROON_PHONE_NUMBER = "^\\+237\\d{9}\$".toRegex()
    val passwordMinLength = 8
}