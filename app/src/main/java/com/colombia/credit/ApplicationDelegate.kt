package com.colombia.credit

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.bigdata.lib.BigDataManager
import com.bigdata.lib.net.NetConfigDataInterface
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.AppEnv
import com.colombia.credit.app.AppInjector
import com.colombia.credit.expand.getUserToken
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.webview.WebViewPool
import com.colombia.credit.util.GPInfoUtils
import com.google.gson.JsonObject
import com.util.lib.ImageInfoUtil
import com.util.lib.UtilInit
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketException

object ApplicationDelegate {

    private var mWeakContext: WeakReference<Context>? = null

    fun init(application: Application) {
        mWeakContext = WeakReference(application.applicationContext)
        UtilInit.get().setContext(application)
        AppInjector.init(application)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setRxjavaErrorHandler()
        initBigData()
        WebViewPool.INSTANCE.init(application)
    }

    fun getContext() = mWeakContext?.get()

    private fun initBigData() {
        BigDataManager.get().setNetDataListener(object : NetConfigDataInterface {
            override fun getContext(): Context = LoanApplication.getAppContext()

            override fun getGaid(): String = GPInfoUtils.getGdid()

            override fun isDebug(): Boolean = AppEnv.DEBUG

            override fun isAppFront(): Boolean =
                SharedPrefUser.getBoolean(SharedPrefKeyManager.KEY_APP_FRONT_BACK_TAG, false)

            override fun getAppToken(): String = getUserToken()

            override fun getBigUrl(): String = Constant.BIG_DATA_URL

            override fun addBaseParams(jobj: JsonObject) {
                // ocrPhotoExif
                jobj.addProperty(
                    "p4yg",
                    ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FRONT)
                )
                // faceExif
                jobj.addProperty(
                    "Bgp3rnTyWw",
                    ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FACE)
                )

            }
        })
    }

    private fun setRxjavaErrorHandler() {
        if (AppEnv.DEBUG) return
        RxJavaPlugins.setErrorHandler(Consumer<Throwable> {
            logger_e("debug_TheApplication", "setErrorHandler == $it")
            var e: Throwable? = it
            if (it is UndeliverableException) {
                e = it.cause
            }
            if ((e is IOException) || (e is SocketException) || e is InterruptedException) {
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
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
            }
            logger_d("debug_TheApplication", e.toString())
        })
    }
}