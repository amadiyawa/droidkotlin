package com.amadiyawa.feature_billing.domain.usecase

import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow

class GetBillsUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(): Flow<List<Bill>> = repository.getBills()
}