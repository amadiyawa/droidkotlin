package com.amadiyawa.feature_users.domain.usecase

import com.amadiyawa.feature_users.domain.repository.UserRepository

internal class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(uuid: String) = userRepository.getUserByUuid(uuid)
}