package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.StreetEntityModel
import com.amadiyawa.feature_users.domain.model.Street
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StreetApiModel(
    @SerialName("number") val number: Int,
    @SerialName("name") val name: String
)

internal fun StreetApiModel.toDomainModel() = Street(
    number = this.number,
    name = this.name
)

internal fun StreetApiModel.toEntityModel() = StreetEntityModel(
    number = this.number,
    name = this.name
)