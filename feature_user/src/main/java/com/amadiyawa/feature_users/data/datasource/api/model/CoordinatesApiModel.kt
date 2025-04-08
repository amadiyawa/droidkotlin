package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.CoordinatesEntityModel
import com.amadiyawa.feature_users.domain.model.Coordinates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoordinatesApiModel(
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String
)

internal fun CoordinatesApiModel.toDomainModel() = Coordinates(
    latitude = this.latitude,
    longitude = this.longitude
)

internal fun CoordinatesApiModel.toEntityModel() = CoordinatesEntityModel(
    latitude = this.latitude,
    longitude = this.longitude
)