package com.amadiyawa.feature_billing.presentation.state

sealed class InvoiceEvent {
    data class ShowError(val message: String) : InvoiceEvent()
    data class NavigateToDetail(val billId: String) : InvoiceEvent()
    data class PaymentSuccess(val billId: String) : InvoiceEvent()
    data object DataRefreshed : InvoiceEvent()
}