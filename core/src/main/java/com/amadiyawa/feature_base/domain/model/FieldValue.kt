package com.amadiyawa.feature_base.domain.model

sealed interface FieldValue {
    val value: Any

    data class Text(override val value: String) : FieldValue
    data class BooleanValue(override val value: Boolean) : FieldValue
}