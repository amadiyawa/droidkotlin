package com.amadiyawa.feature_auth.presentation

import com.amadiyawa.feature_auth.presentation.screen.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::SignInViewModel)
}