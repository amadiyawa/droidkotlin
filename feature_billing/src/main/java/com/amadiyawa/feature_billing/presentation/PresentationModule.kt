package com.amadiyawa.feature_billing.presentation

import com.amadiyawa.feature_base.presentation.navigation.FeatureNavigationApi
import com.amadiyawa.feature_base.presentation.navigation.NavigationRegistry
import com.amadiyawa.feature_billing.presentation.navigation.InvoiceNavigationApi
import com.amadiyawa.feature_billing.presentation.viewmodel.InvoiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import timber.log.Timber

internal val presentationModule = module {
    // Log module loading
    Timber.d("Loading invoice feature module")

    // Register the navigation API
    single {
        InvoiceNavigationApi()
    } bind FeatureNavigationApi::class

    // Register the feature in the navigation registry
    // This is done as a side effect when the module is loaded
    factory(named("invoiceFeatureRegistration")) {
        val registry = get<NavigationRegistry>()
        val api = get<InvoiceNavigationApi>()
        registry.registerFeature(api)
        Timber.d("Registered InvoiceNavigationApi with NavigationRegistry")

        // Return a dummy value to satisfy Koin
        true
    }

    // ViewModel
    viewModel {
        InvoiceViewModel(
            getBillsUseCase = get(),
            getBillByIdUseCase = get(),
            getPaymentsForBillUseCase = get(),
            generatePaymentUseCase = get(),
            updateBillStatusUseCase = get(),
            refreshInvoiceDataUseCase = get()
        )
    }
}