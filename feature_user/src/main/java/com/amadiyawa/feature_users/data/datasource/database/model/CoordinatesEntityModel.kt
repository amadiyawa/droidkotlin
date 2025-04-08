package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Coordinates
import kotlinx.serialization.Serializable

@Serializable
internal data class CoordinatesEntityModel(
    val latitude: String,
    val longitude: String
)

internal fun CoordinatesEntityModel.toDomainModel() = Coordinates(
    latitude = this.latitude,
    longitude = this.longitude
)