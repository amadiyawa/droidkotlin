package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.TimezoneEntityModel
import com.amadiyawa.feature_users.domain.model.Timezone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TimezoneApiModel(
    @SerialName("offset") val offset: String,
    @SerialName("description") val description: String
)

internal fun TimezoneApiModel.toDomainModel() = Timezone(
    offset = this.offset,
    description = this.description
)

internal fun TimezoneApiModel.toEntityModel() = TimezoneEntityModel(
    offset = this.offset,
    description = this.description
)