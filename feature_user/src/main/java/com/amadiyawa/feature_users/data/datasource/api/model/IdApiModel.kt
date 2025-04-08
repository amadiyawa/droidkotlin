package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.IdEntityModel
import com.amadiyawa.feature_users.domain.model.Id
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class IdApiModel(
    @SerialName("name") val name: String,
    @SerialName("value") val value: String
)

internal fun IdApiModel.toDomainModel() = Id(
    name = this.name,
    value = this.value
)

internal fun IdApiModel.toEntityModel() = IdEntityModel(
    name = this.name,
    value = this.value
)