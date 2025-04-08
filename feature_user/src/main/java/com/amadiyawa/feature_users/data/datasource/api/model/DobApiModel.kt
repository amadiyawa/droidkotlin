package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.DobEntityModel
import com.amadiyawa.feature_users.domain.model.Dob
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DobApiModel(
    @SerialName("date") val date: String,
    @SerialName("age") val age: Int
)

internal fun DobApiModel.toDomainModel() = Dob(
    date = this.date,
    age = this.age
)

internal fun DobApiModel.toEntityModel() = DobEntityModel(
    date = this.date,
    age = this.age
)