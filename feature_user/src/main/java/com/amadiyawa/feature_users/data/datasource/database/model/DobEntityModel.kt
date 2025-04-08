package com.amadiyawa.feature_users.data.datasource.database.model

import com.amadiyawa.feature_users.domain.model.Dob
import kotlinx.serialization.Serializable

@Serializable
internal data class DobEntityModel(
    val date: String,
    val age: Int
)

internal fun DobEntityModel.toDomainModel() = Dob(
    date = this.date,
    age = this.age
)