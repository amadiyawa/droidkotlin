package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.NameEntityModel
import com.amadiyawa.feature_users.domain.model.Name
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NameApiModel(
    @SerialName("title") val title: String,
    @SerialName("first") val first: String,
    @SerialName("last") val last: String
)

internal fun NameApiModel.toDomainModel() = Name(
    title = this.title,
    first = this.first,
    last = this.last
)

internal fun NameApiModel.toEntityModel() = NameEntityModel(
    title = this.title,
    first = this.first,
    last = this.last
)