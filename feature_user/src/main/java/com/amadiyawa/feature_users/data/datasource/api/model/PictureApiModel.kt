package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.PictureEntityModel
import com.amadiyawa.feature_users.domain.model.Picture
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PictureApiModel(
    @SerialName("large") val large: String,
    @SerialName("medium") val medium: String,
    @SerialName("thumbnail") val thumbnail: String
)

internal fun PictureApiModel.toDomainModel() = Picture(
    large = this.large,
    medium = this.medium,
    thumbnail = this.thumbnail
)

internal fun PictureApiModel.toEntityModel() = PictureEntityModel(
    large = this.large,
    medium = this.medium,
    thumbnail = this.thumbnail
)