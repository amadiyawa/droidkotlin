package com.amadiyawa.feature_auth.presentation

import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationViewModel
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInViewModelOld
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::SignInViewModelOld)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::OtpVerificationViewModel)
}