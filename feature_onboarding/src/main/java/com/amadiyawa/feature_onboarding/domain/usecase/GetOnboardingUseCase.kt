package com.amadiyawa.feature_onboarding.domain.usecase

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.domain.model.OnboardingScreen
import com.amadiyawa.feature_onboarding.domain.repository.OnboardingRepository

/**
 * UseCase class responsible for fetching the onboard list.
 *
 * This UseCase is responsible for fetching the onboard list asynchronously
 * by interacting with the OnboardRepository. It returns the result as a
 * `Result<List<Onboard>>`, allowing the caller to handle success or error states.
 *
 * @property onboardingRepository The repository used to fetch the onboard list.
 */
internal class GetOnboardingUseCase(private val onboardingRepository: OnboardingRepository) {
    suspend operator fun invoke(): OperationResult<List<OnboardingScreen>> {
        return onboardingRepository.getOnboardList()
    }
}