package com.amadiyawa.feature_users.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoApiModel(
    @SerialName("seed") val seed: String,
    @SerialName("results") val results: Int,
    @SerialName("page") val page: Int,
    @SerialName("version") val version: String
)