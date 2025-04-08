package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Registered
import kotlinx.serialization.Serializable

@Serializable
internal data class RegisteredEntityModel(
    val date: String,
    val age: Int
)

internal fun RegisteredEntityModel.toDomainModel() = Registered(
    date = this.date,
    age = this.age
)