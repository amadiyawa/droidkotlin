package com.amadiyawa.feature_base.presentation.util

import android.content.Context

class ResourceProvider(private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}