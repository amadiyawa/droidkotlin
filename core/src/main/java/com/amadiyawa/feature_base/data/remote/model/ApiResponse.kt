package com.amadiyawa.feature_base.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleApiResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String? = null
)