package com.amadiyawa.feature_auth.data.mapper

import com.amadiyawa.feature_auth.data.datasource.api.model.SignUpApiModel
import com.amadiyawa.feature_auth.domain.model.SignUp

internal fun SignUpApiModel.toDomainModel() = SignUp(
    verificationId = this.verificationId,
    recipient = this.recipient,
    expiresIn = this.expiresIn
)