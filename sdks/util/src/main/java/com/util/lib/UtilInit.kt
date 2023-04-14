package com.util.lib

import android.content.Context
import android.content.pm.ApplicationInfo

class UtilInit {

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.NONE) { UtilInit() }

        fun get() = instance
    }

    private var mContext: Context? = null

    private var isDebug: Boolean = false

    fun setContext(context: Context): UtilInit {
        this.mContext = context.applicationContext
        val appInfo = context.applicationInfo
        isDebug = appInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return this
    }

    fun setLogDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    fun isDebug() = isDebug

    fun getContext() = mContext
}

//fun getContext() : Context {
//    val ctx = UtilInit.get().getContext() ?: throw IllegalStateException("please call first UtilInit.get().setContext()")
//    return ctx
//}