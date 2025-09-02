package com.kira.learning

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kira.learning.DeferredStartup.Task
import com.kira.learning.DeferredStartup.Phase
import com.util.lib.UtilInit
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import java.io.IOException
import java.lang.ref.WeakReference

object ApplicationDelegate {

    private var mWeakContext: WeakReference<Context>? = null
    @Volatile
    private var inited = false

    fun init(application: Application) {
        if (inited) return
        inited = true
        mWeakContext = WeakReference(application.applicationContext)
        // 立即执行的轻量操作
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        // 重/中等初始化延迟到首帧后由 LoanApplication 统一调度
        registerDeferred(application)
    }

    private fun registerDeferred(application: Application) {
        DeferredStartup.register(Task("UtilInit", Phase.LIGHT) {
            UtilInit.get().setContext(application)
        })
        // WebViewPool 现已惰性初始化，不再预注册（需要预热时可单独调用 WebViewPool.INSTANCE.init(application)）
    }

    fun getContext() = mWeakContext?.get()

}