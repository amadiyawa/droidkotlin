package com.amadiyawa.feature_users.data.datasource.api.model

import com.amadiyawa.feature_users.data.datasource.database.model.LocationEntityModel
import com.amadiyawa.feature_users.data.util.PostcodeSerializer
import com.amadiyawa.feature_users.domain.model.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LocationApiModel(
    @SerialName("street") val street: StreetApiModel,
    @SerialName("city") val city: String,
    @SerialName("state") val state: String,
    @SerialName("country") val country: String,
    @Serializable(with = PostcodeSerializer::class)
    @SerialName("postcode") val postcode: String,
    @SerialName("coordinates") val coordinates: CoordinatesApiModel,
    @SerialName("timezone") val timezone: TimezoneApiModel
)

internal fun LocationApiModel.toDomainModel() = Location(
    street = this.street.toDomainModel(),
    city = this.city,
    state = this.state,
    country = this.country,
    postcode = this.postcode,
    coordinates = this.coordinates.toDomainModel(),
    timezone = this.timezone.toDomainModel()
)

internal fun LocationApiModel.toEntityModel() = LocationEntityModel(
    street = this.street.toEntityModel(),
    city = this.city,
    state = this.state,
    country = this.country,
    postcode = this.postcode,
    coordinates = this.coordinates.toEntityModel(),
    timezone = this.timezone.toEntityModel()
)