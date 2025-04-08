package com.amadiyawa.feature_users

import com.amadiyawa.feature_users.data.dataModule
import com.amadiyawa.feature_users.domain.domainModule
import com.amadiyawa.feature_users.presentation.presentationModule

val featureUserModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)