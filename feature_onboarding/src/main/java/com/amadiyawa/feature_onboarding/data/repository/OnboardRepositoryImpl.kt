package com.amadiyawa.feature_onboarding.data.repository

import com.amadiyawa.feature_base.domain.result.OperationResult
import com.amadiyawa.feature_onboarding.data.util.loadOnboardList
import com.amadiyawa.feature_onboarding.domain.model.Onboard
import com.amadiyawa.feature_onboarding.domain.repository.OnboardRepository

/**
 *  This internal (private) function is used to
 *  take the Data of loadOnboardList-Objects which comes from the Onboard
 *  Class in domain/model an its Interface OnboardRepository
 */
internal class OnboardRepositoryImpl : OnboardRepository {
    override suspend fun getOnboardList(): OperationResult<List<Onboard>> {

        // The Result.Success is used to return the Data
        return OperationResult.Success(loadOnboardList())
    }
}