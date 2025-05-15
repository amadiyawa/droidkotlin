package com.amadiyawa.feature_billing.domain.usecase

import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository

class RefreshInvoiceDataUseCase(private val repository: InvoiceRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshData()
}