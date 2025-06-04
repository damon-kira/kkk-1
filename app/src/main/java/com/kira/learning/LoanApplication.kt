package com.kira.learning

import android.content.Context
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.MultiDexApplication
import com.cache.lib.CacheInit
import com.chaquo.python.PyObject
import com.kira.learning.app.AppEnv
import com.kira.learning.module.python.PythonExecutor
import com.project.util.AesConstant
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LoanApplication : MultiDexApplication(), CameraXConfig.Provider {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
        lateinit var module: PyObject
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this
        CacheInit.get().setContext(this).setDebug(AppEnv.DEBUG)
        initAes()
//        initLeakCanary()
        ApplicationDelegate.init(this)
//        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id))
//        PushManagerFactory.init(this)
//        PushManagerFactory.getGaid(this)
//        AdjustManager.init(this, AppEnv.DEBUG)

        PythonExecutor.initialize(this)
    }

//    private fun initLeakCanary() {
//        LeakCanary.config = LeakCanary.config.copy(
//            dumpHeapWhenDebugging = false,  // 调试时不转储堆
//            retainedVisibleThreshold = 5    // 内存泄漏对象阈值
//        )
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