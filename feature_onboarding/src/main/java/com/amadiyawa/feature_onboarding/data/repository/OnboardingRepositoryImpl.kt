package com.amadiyawa.feature_onboarding.data.repository

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.domain.model.OnboardingScreen
import com.amadiyawa.feature_onboarding.domain.repository.OnboardingRepository
import com.amadiyawa.onboarding.R
import timber.log.Timber

internal class OnboardingRepositoryImpl : OnboardingRepository {
    override suspend fun getOnboardList(): OperationResult<List<OnboardingScreen>> {
        return try {
            val onboading =  listOf(
                OnboardingScreen(
                    id = 0,
                    titleResId = R.string.onboard_title_1,
                    descriptionResId = R.string.onboard_description_1,
                    imageResId = R.drawable.onboard_image_1
                ),
                OnboardingScreen(
                    id = 1,
                    titleResId = R.string.onboard_title_2,
                    descriptionResId = R.string.onboard_description_2,
                    imageResId = R.drawable.onboard_image_2
                ),
                OnboardingScreen(
                    id = 2,
                    titleResId = R.string.onboard_title_3,
                    descriptionResId = R.string.onboard_description_3,
                    imageResId = R.drawable.onboard_image_3
                ),
                OnboardingScreen(
                    id = 3,
                    titleResId = R.string.onboard_title_4,
                    descriptionResId = R.string.onboard_description_4,
                    imageResId = R.drawable.onboard_image_4
                ),
                OnboardingScreen(
                    id = 4,
                    titleResId = R.string.onboard_title_5,
                    descriptionResId = R.string.onboard_description_5,
                    imageResId = R.drawable.onboard_image_5
                )
            )
            OperationResult.Success(onboading)
        } catch (e: Exception) {
            Timber.e(e, "Simulated error during sign-in")
            OperationResult.error(
                throwable = e,
                message = "Simulated system error"
            )
        }
    }
}