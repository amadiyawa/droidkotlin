package com.amadiyawa.feature_onboarding.data.util

import com.amadiyawa.feature_onboarding.domain.model.Onboard
import com.amadiyawa.onboarding.R

/**
 * Its a internal (only visible in this module, like: private)
 * Function where a List of Onboard-Objects is
 * created.
 */
internal fun loadOnboardList(): List<Onboard> {
    return listOf(
        Onboard(
            id = 0,
            titleResId = R.string.onboard_title_1,
            descriptionResId = R.string.onboard_description_1,
            imageResId = R.drawable.onboard_image_1
        ),
        Onboard(
            id = 1,
            titleResId = R.string.onboard_title_2,
            descriptionResId = R.string.onboard_description_2,
            imageResId = R.drawable.onboard_image_2
        ),
        Onboard(
            id = 2,
            titleResId = R.string.onboard_title_3,
            descriptionResId = R.string.onboard_description_3,
            imageResId = R.drawable.onboard_image_3
        ),
        Onboard(
            id = 3,
            titleResId = R.string.onboard_title_4,
            descriptionResId = R.string.onboard_description_4,
            imageResId = R.drawable.onboard_image_4
        ),
        Onboard(
            id = 4,
            titleResId = R.string.onboard_title_5,
            descriptionResId = R.string.onboard_description_5,
            imageResId = R.drawable.onboard_image_5
        )
    )
}