package com.amadiyawa.feature_billing.presentation.state

import com.amadiyawa.feature_billing.domain.model.PaymentMethod

sealed class InvoiceAction {
    data object LoadBills : InvoiceAction()
    data class SetFilter(val filter: BillFilter) : InvoiceAction()
    data class SearchBills(val query: String) : InvoiceAction()
    data class SelectBill(val billId: String) : InvoiceAction()
    data class SetSortOrder(val sortOrder: SortOrder) : InvoiceAction()
    data object RefreshData : InvoiceAction()
    data object ClearSelection : InvoiceAction()
    data class GeneratePayment(
        val billId: String,
        val amount: Double,
        val method: PaymentMethod
    ) : InvoiceAction()
}