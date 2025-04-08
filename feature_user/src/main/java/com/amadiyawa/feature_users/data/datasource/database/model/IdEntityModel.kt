package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Id
import kotlinx.serialization.Serializable

@Serializable
internal data class IdEntityModel(
    val name: String,
    val value: String
)

internal fun IdEntityModel.toDomainModel() = Id(
    name = this.name,
    value = this.value
)