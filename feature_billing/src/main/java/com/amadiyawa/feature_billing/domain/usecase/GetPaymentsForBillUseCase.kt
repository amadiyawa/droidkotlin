package com.amadiyawa.feature_billing.domain.usecase

import com.amadiyawa.feature_billing.domain.model.Payment
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow

class GetPaymentsForBillUseCase(private val repository: InvoiceRepository) {
    operator fun invoke(billId: String): Flow<List<Payment>> = repository.getPaymentsForBill(billId)
}