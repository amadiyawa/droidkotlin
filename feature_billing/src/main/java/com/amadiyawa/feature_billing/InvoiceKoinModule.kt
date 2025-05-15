package com.amadiyawa.feature_billing

import com.amadiyawa.feature_billing.data.dataModule
import com.amadiyawa.feature_billing.domain.domainModule
import com.amadiyawa.feature_billing.presentation.presentationModule

val featureBillingModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)