package com.amadiyawa.feature_base.common.resources

import android.content.Context

class AndroidStringResourceProvider(private val appContext: Context) : StringResourceProvider {
    override fun getString(resId: Int): String = appContext.getString(resId)
    override fun getString(resId: Int, vararg formatArgs: Any): String =
        appContext.getString(resId, *formatArgs)
}