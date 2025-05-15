package com.amadiyawa.feature_billing.domain.usecase

import com.amadiyawa.feature_billing.domain.model.Payment
import com.amadiyawa.feature_billing.domain.model.PaymentMethod
import com.amadiyawa.feature_billing.domain.repository.InvoiceRepository

class GeneratePaymentUseCase(private val repository: InvoiceRepository) {
    suspend operator fun invoke(
        billId: String,
        amount: Double,
        method: PaymentMethod
    ): Result<Payment> = repository.generatePayment(billId, amount, method)
}