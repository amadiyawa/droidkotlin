package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.RegisteredEntityModel
import com.amadiyawa.feature_users.domain.model.Registered
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisteredApiModel(
    @SerialName("date") val date: String,
    @SerialName("age") val age: Int
)

internal fun RegisteredApiModel.toDomainModel() = Registered(
    date = this.date,
    age = this.age
)

internal fun RegisteredApiModel.toEntityModel() = RegisteredEntityModel(
    date = this.date,
    age = this.age
)