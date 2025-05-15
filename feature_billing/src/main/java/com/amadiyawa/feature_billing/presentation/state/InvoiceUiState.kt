package com.amadiyawa.feature_billing.presentation.state

import com.amadiyawa.feature_billing.domain.model.Bill
import com.amadiyawa.feature_billing.domain.model.Payment

data class InvoiceUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val bills: List<Bill> = emptyList(),
    val filteredBills: List<Bill> = emptyList(),
    val selectedBill: Bill? = null,
    val payments: List<Payment> = emptyList(),
    val activeFilter: BillFilter = BillFilter.ALL,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false,
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)

enum class BillFilter {
    ALL,
    PENDING,
    PAID,
    OVERDUE
}

enum class SortOrder {
    DATE_ASC,
    DATE_DESC,
    AMOUNT_ASC,
    AMOUNT_DESC
}