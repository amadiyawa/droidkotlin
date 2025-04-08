package com.amadiyawa.feature_users.domain.model

internal data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: Coordinates,
    val timezone: Timezone,
)
