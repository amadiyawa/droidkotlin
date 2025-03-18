package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.runtime.Composable
import com.amadiyawa.paygo.base.R

@Composable
fun DataNotFoundAnim() {
    LabeledAnimation(R.string.data_not_found, R.raw.lottie_error_screen)
}