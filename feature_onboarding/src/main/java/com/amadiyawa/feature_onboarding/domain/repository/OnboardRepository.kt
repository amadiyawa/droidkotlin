package com.amadiyawa.feature_onboarding.domain.repository

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.domain.model.Onboard

/**
 * Repository interface for managing onboarding data.
 */
internal interface OnboardRepository {
    suspend fun getOnboardList(): OperationResult<List<Onboard>>
}