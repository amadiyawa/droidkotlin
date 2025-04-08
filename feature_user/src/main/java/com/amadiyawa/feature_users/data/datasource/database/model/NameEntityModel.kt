package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Name
import kotlinx.serialization.Serializable

@Serializable
internal data class NameEntityModel(
    val title: String,
    val first: String,
    val last: String
)

internal fun NameEntityModel.toDomainModel() = Name(
    title = this.title,
    first = this.first,
    last = this.last
)