package com.amadiyawa.feature_onboarding.domain.model

import kotlinx.serialization.Serializable

/**
 * Data class representing an onboarding item.
 *
 * @property id The unique identifier for the onboarding item.
 * @property titleResId The resource ID for the title of the onboarding item.
 * @property descriptionResId The resource ID for the description of the onboarding item.
 * @property imageResId The resource ID for the image of the onboarding item.
 */
@Serializable
data class OnboardingScreen(
    val id: Int,
    val titleResId: Int,
    val descriptionResId: Int,
    val imageResId: Int
)