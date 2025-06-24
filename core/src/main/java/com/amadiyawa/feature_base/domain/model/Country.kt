package com.amadiyawa.feature_base.domain.model

data class Country(
    val code: String,
    val nameResId: Int,
    val dialCode: String,
    val flagEmoji: String,
    val phoneExample: String
)