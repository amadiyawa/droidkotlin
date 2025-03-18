package com.amadiyawa.feature_onboarding.domain.repository

import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_onboarding.domain.model.Onboard

/**
 * Its an Interface which uses a Asynchronous (suspend)
 * method to return a List of Onboard-Objects as a Result
 */
internal interface OnboardRepository {
    suspend fun getOnboardList(): Result<List<Onboard>>
}