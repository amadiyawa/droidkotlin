package com.amadiyawa.feature_base.domain.resources

interface ResourceProvider {
    fun getString(resId: Int, vararg formatArgs: Any): String
    fun getStringArray(resId: Int): Array<String>
}