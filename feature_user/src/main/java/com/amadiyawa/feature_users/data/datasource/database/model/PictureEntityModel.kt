package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Picture
import kotlinx.serialization.Serializable

@Serializable
internal data class PictureEntityModel(
    val large: String,
    val medium: String,
    val thumbnail: String
)

internal fun PictureEntityModel.toDomainModel() = Picture(
    large = this.large,
    medium = this.medium,
    thumbnail = this.thumbnail
)