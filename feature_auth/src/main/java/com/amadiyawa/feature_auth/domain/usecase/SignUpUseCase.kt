package com.amadiyawa.feature_auth.domain.usecase

import com.amadiyawa.feature_auth.data.datasource.api.model.SignUpRequest
import com.amadiyawa.feature_auth.domain.model.SignUp
import com.amadiyawa.feature_auth.domain.repository.AuthRepository
import com.amadiyawa.feature_base.domain.result.OperationResult

internal class SignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(signUpRequest: SignUpRequest): OperationResult<SignUp> {
        return authRepository.signUp(signUpRequest = signUpRequest)
    }
}