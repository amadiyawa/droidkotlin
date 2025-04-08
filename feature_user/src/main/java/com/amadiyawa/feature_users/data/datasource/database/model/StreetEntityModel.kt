package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Street
import kotlinx.serialization.Serializable

@Serializable
internal data class StreetEntityModel(
    val number: Int,
    val name: String
)

internal fun StreetEntityModel.toDomainModel() = Street(
    number = this.number,
    name = this.name
)