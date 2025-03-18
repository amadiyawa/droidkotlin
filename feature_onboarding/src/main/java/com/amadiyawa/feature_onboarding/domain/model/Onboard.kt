package com.amadiyawa.feature_onboarding.domain.model

import kotlinx.serialization.Serializable

@Serializable
/**
 * Its a Class where we store the data of the Onboard,
 * It will be used as Container, how data will be structured.
 */
data class Onboard(
    val id: Int,    // id of the Onboard
    val titleResId: Int, // title of the Onboard
    val descriptionResId: Int, // description of the Onboard
    val imageResId: Int // image of the Onboard
)
