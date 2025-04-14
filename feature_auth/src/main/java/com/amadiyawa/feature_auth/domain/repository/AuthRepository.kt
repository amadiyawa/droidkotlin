package com.amadiyawa.feature_auth.domain.repository

import com.amadiyawa.feature_auth.domain.model.SignIn
import com.amadiyawa.feature_auth.data.datasource.api.model.SignInRequest
import com.amadiyawa.feature_base.domain.result.OperationResult

internal interface AuthRepository {
    suspend fun signIn(signInRequest: SignInRequest): OperationResult<SignIn>
}