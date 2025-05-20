package com.kira.learning

import android.content.Context
import android.content.res.Configuration
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.MultiDexApplication
import com.cache.lib.CacheInit
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.kira.learning.app.AppEnv
import com.kira.learning.module.python.PythonExecutor
import com.kira.learning.util.LanguageUtils
import com.project.util.AesConstant
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale


@HiltAndroidApp
class LoanApplication: MultiDexApplication(), CameraXConfig.Provider  {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
        private lateinit var python: Python
        lateinit var module: PyObject
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this
        CacheInit.get().setContext(this).setDebug(AppEnv.DEBUG)
        initAes()
//        initPython()
        ApplicationDelegate.init(this)
//        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id))
//        PushManagerFactory.init(this)
//        PushManagerFactory.getGaid(this)
//        AdjustManager.init(this, AppEnv.DEBUG)
        PythonExecutor.initialize(this)
//        LanguageUtils.attachBaseContext(this)
    }

//    override fun attachBaseContext(base: Context?) {
////        super.attachBaseContext(base)
//        val locale = Locale("zh") // 或 Locale("zh", "CN")
//        val config = Configuration(base?.resources?.configuration).apply {
//            setLocale(locale)
//            setLayoutDirection(locale)
//        }
//        super.attachBaseContext(base?.createConfigurationContext(config))
//    }
//    private fun initPython() {
//        if (!Python.isStarted()) Python.start(AndroidPlatform(mAppContext))
//        python = Python.getInstance()
//        module = python.getModule("execute")
//    }

    private fun initAes() {
        AesConstant.AES_SECRET = Constant.API_SECRET
        AesConstant.apiKey = Constant.API_KEY
        AesConstant.apiIv = Constant.API_IV
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}