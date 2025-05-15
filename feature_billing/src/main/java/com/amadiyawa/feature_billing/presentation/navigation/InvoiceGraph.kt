package com.amadiyawa.feature_billing.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.amadiyawa.feature_billing.presentation.screen.billingdetail.InvoiceDetailScreen
import com.amadiyawa.feature_billing.presentation.screen.billinglist.InvoiceListScreen
import timber.log.Timber

fun NavGraphBuilder.invoiceGraph(
    onNavigateToInvoiceDetail: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Timber.d("Building invoice graph with callbacks")

    navigation(
        startDestination = InvoiceListNavigation.destination,
        route = InvoiceListNavigation.route
    ) {
        Timber.d("Setting up invoice navigation - startDestination: ${InvoiceListNavigation.destination}, route: ${InvoiceListNavigation.route}")

        Timber.d("Adding composable for: ${InvoiceListNavigation.destination}")
        composable(InvoiceListNavigation.destination) {
            Timber.d("Rendering InvoiceListScreen")
            InvoiceListScreen(onInvoiceClick = onNavigateToInvoiceDetail)
        }

        Timber.d("Adding composable for: ${InvoiceDetailNavigation.route}")
        composable(InvoiceDetailNavigation.route) { backStackEntry ->
            val invoiceId = backStackEntry.arguments?.getString("invoiceId")
            Timber.d("Preparing InvoiceDetailScreen with id: $invoiceId")
            if (invoiceId != null) {
                InvoiceDetailScreen(invoiceId = invoiceId, onBackClick = onBackClick)
            }
        }

        Timber.d("Finished setting up invoice navigation components")
    }

    Timber.d("Invoice graph with callbacks built")
}

fun NavGraphBuilder.invoiceGraph(navController: NavHostController) {
    Timber.d("Building invoice graph with NavHostController")

    invoiceGraph(
        onNavigateToInvoiceDetail = {
            Timber.d("Navigation to invoice detail requested: $it")
            navController.navigate(InvoiceListNavigation.detailRoute(it))
        },
        onBackClick = {
            Timber.d("Back navigation from invoice requested")
            navController.popBackStack()
        }
    )

    Timber.d("Invoice graph with NavHostController built")
}