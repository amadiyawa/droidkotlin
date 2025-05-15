package com.amadiyawa.feature_billing.domain

import com.amadiyawa.feature_billing.domain.usecase.GeneratePaymentUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetBillByIdUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetBillsUseCase
import com.amadiyawa.feature_billing.domain.usecase.GetPaymentsForBillUseCase
import com.amadiyawa.feature_billing.domain.usecase.RefreshInvoiceDataUseCase
import com.amadiyawa.feature_billing.domain.usecase.UpdateBillStatusUseCase
import org.koin.dsl.module

internal val domainModule = module {
    // Use Cases
    factory { GetBillsUseCase(get()) }
    factory { GetBillByIdUseCase(get()) }
    factory { GetPaymentsForBillUseCase(get()) }
    factory { GeneratePaymentUseCase(get()) }
    factory { UpdateBillStatusUseCase(get()) }
    factory { RefreshInvoiceDataUseCase(get()) }
}