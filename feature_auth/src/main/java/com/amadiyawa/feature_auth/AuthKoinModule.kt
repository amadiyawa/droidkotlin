package com.amadiyawa.feature_auth

import com.amadiyawa.feature_auth.data.dataModule
import com.amadiyawa.feature_auth.domain.domainModule
import com.amadiyawa.feature_auth.presentation.presentationModule

val featureAuthModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)