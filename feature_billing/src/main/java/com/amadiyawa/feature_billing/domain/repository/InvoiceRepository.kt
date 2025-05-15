package com.amadiyawa.feature_billing.domain.repository

import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.BillStatus
import com.amadiyawa.feature_billing.domain.model.Payment
import com.amadiyawa.feature_billing.domain.model.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun getBills(): Flow<List<Bill>>
    fun getBillById(id: String): Flow<Bill?>
    fun getPaymentsForBill(billId: String): Flow<List<Payment>>
    suspend fun generatePayment(billId: String, amount: Double, method: PaymentMethod): Result<Payment>
    suspend fun updateBillStatus(billId: String, status: BillStatus): Result<Unit>
    suspend fun refreshData(): Result<Unit>
}