package com.kira.learning.module.supereditor.provider.resources

import android.content.Context
import com.kira.ui.core.provider.resources.StringProvider

class StringProviderImpl(private val context: Context) : StringProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}