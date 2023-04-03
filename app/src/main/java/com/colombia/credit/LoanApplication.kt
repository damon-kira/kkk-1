package com.colombia.credit

import android.app.Application
import android.content.Context
import com.cache.lib.CacheInit
import com.colombia.credit.module.webview.WebViewPool
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by weishl on 2023/3/23
 *
 */
@HiltAndroidApp
class LoanApplication: Application() {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
    }


    override fun onCreate() {
        super.onCreate()
        mAppContext = this
        CacheInit.get().setContext(this)
        WebViewPool.INSTANCE.init(this, false)
    }
}