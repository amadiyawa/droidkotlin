package com.amadiyawa.feature_billing.domain.usecase

import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository

class UpdateBillStatusUseCase(private val repository: InvoiceRepository) {
    suspend operator fun invoke(billId: String, status: BillStatus): Result<Unit> =
        repository.updateBillStatus(billId, status)
}