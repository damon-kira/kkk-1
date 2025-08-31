package com.kira.learning

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.kira.learning.di.AppInjector
import com.kira.learning.xml.modules.webview.WebViewPool
import com.util.lib.UtilInit
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.io.IOException
import java.lang.ref.WeakReference

object ApplicationDelegate {

    private var mWeakContext: WeakReference<Context>? = null

    fun init(application: Application) {
        mWeakContext = WeakReference(application.applicationContext)
        UtilInit.get().setContext(application)
        AppInjector.init(application)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setRxjavaErrorHandler()
        WebViewPool.INSTANCE.init(application)
    }

    fun getContext() = mWeakContext?.get()

    private fun setRxjavaErrorHandler() {
        if (AppEnv.DEBUG) return
        RxJavaPlugins.setErrorHandler(Consumer<Throwable> {
            logger_e("debug_TheApplication", "setErrorHandler == $it")
            var e: Throwable? = it
            if (it is UndeliverableException) {
                e = it.cause
            }
            if (e is IOException || e is InterruptedException) {
                return@Consumer
            }
            if ((e is NullPointerException) || (e is IllegalArgumentException)) {
                // that's likely a bug in the application
                return@Consumer
//                Thread.currentThread().uncaughtExceptionHandler
//                        .uncaughtException(Thread.currentThread(), e)
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(
                    Thread.currentThread(), e
                )
            }
            logger_d("debug_TheApplication", e.toString())
        })
    }
}