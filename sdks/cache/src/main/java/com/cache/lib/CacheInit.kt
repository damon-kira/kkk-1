package com.cache.lib

import android.content.Context
import android.content.pm.ApplicationInfo

class CacheInit {

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.NONE) { CacheInit() }

        fun get() = instance
    }

    private var mContext: Context? = null

    private var isDebug: Boolean = false

    fun setContext(context: Context): CacheInit {
        this.mContext = context.applicationContext
        val appInfo = context.applicationInfo
        isDebug = appInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return this
    }

    fun isDebug() = isDebug

    fun getContext() = mContext
}

fun getContext() : Context {
    val ctx = CacheInit.get().getContext() ?: throw IllegalStateException("please call first UtilInit.get().setContext()")
    return ctx
}