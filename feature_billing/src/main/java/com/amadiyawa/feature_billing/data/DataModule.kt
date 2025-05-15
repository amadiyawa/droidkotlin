package com.amadiyawa.feature_billing.data

import com.amadiyawa.feature_billing.data.repository.FakeInvoiceRepository
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository
import org.koin.dsl.module

internal val dataModule = module {
    // Repository
    single<InvoiceRepository> { FakeInvoiceRepository(userSessionManager = get()) }
}