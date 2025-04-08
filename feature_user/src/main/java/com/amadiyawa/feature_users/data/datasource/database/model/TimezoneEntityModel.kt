package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Timezone
import kotlinx.serialization.Serializable

@Serializable
internal data class TimezoneEntityModel(
    val offset: String,
    val description: String
)

internal fun TimezoneEntityModel.toDomainModel() = Timezone(
    offset = this.offset,
    description = this.description
)