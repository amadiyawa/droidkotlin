package com.amadiyawa.feature_auth.presentation

import com.amadiyawa.feature_auth.presentation.screen.forgotpassword.ForgotPasswordViewModel
import com.amadiyawa.feature_auth.presentation.screen.otpverification.OtpVerificationViewModel
import com.amadiyawa.feature_auth.presentation.screen.resetpassword.ResetPasswordViewModel
import com.amadiyawa.feature_auth.presentation.screen.signin.SignInViewModel
import com.amadiyawa.feature_auth.presentation.screen.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::OtpVerificationViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}