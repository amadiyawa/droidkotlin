package com.amadiyawa.feature_profile

import com.amadiyawa.feature_profile.data.dataModule
import com.amadiyawa.feature_profile.domain.domainModule
import com.amadiyawa.feature_profile.presentation.presentationModule

val featureProfileModule = listOf(
    presentationModule,
    domainModule,
    dataModule
)