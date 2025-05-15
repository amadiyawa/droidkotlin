package com.amadiyawa.feature_billing.presentation.navigation

import com.amadiyawa.feature_base.presentation.navigation.AppNavigationDestination

object InvoiceListNavigation : AppNavigationDestination {
    override val route = "invoice_list"
    override val destination = "invoice_list_destination"
    fun detailRoute(invoiceId: String) = "invoice_detail/$invoiceId"
}

object InvoiceDetailNavigation : AppNavigationDestination {
    override val route = "invoice_detail/{invoiceId}"
    override val destination = "invoice_detail_destination"
}