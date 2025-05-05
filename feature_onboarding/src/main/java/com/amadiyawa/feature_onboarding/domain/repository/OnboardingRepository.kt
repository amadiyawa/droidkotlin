package com.amadiyawa.feature_onboarding.domain.repository

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.domain.model.OnboardingScreen

/**
 * A functional interface for managing onboarding data retrieval.
 *
 * This interface defines a single abstract method for fetching a list of onboarding screens.
 * It uses a suspend function to support asynchronous operations and returns an
 * [OperationResult] containing a list of [OnboardingScreen] objects.
 */
fun interface OnboardingRepository {
    suspend fun getOnboardList(): OperationResult<List<OnboardingScreen>>
}