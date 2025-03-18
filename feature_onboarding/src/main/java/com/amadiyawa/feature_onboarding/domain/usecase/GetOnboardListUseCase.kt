package com.amadiyawa.feature_onboarding.domain.usecase

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_onboarding.domain.model.Onboard
import com.amadiyawa.feature_onboarding.domain.repository.OnboardRepository

/**
 * This UseCase is responsible for fetching the onboard list asynchronously
 * by interacting with the OnboardRepository. It returns the result as a
 * `Result<List<Onboard>>`, allowing the caller to handle success or error states.
 */
internal class GetOnboardListUseCase(private val onboardRepository: OnboardRepository) {
    suspend operator fun invoke(): Result<List<Onboard>> {
        return onboardRepository.getOnboardList()
    }
}