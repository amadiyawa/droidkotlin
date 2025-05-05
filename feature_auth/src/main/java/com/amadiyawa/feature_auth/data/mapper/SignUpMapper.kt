package com.amadiyawa.feature_auth.data.mapper

import com.amadiyawa.feature_auth.data.dto.response.SignUpResponse
import com.amadiyawa.feature_auth.domain.model.SignUp

internal fun SignUpResponse.toDomainModel() = SignUp(
    verificationId = this.verificationId,
    recipient = this.recipient,
    expiresIn = this.expiresIn
)