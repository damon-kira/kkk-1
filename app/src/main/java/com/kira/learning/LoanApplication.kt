package com.kira.learning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.MultiDexApplication
import com.cache.lib.CacheInit
//import com.chaquo.python.PyObject
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kira.learning.app.AppEnv
import com.kira.learning.app.AppEnv.Companion.NOTIFICATION_CHANNEL_ID
import com.kira.learning.messaging.KiraFirebaseMessagingService
import com.kira.quickupload.UploadConfiguration
//import com.kira.learning.module.python.PythonExecutor
import com.project.util.AesConstant
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security


@HiltAndroidApp
class LoanApplication : MultiDexApplication(), CameraXConfig.Provider {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
//        lateinit var module: PyObject
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

        // Firebase 初始化
        initFirebase()

        // 本地Python初始化
//        PythonExecutor.initialize(this)
        Security.insertProviderAt(Conscrypt.newProvider(), 1)

        initUpload()
    }

    private fun initUpload() {
        createNotificationChannel()
        UploadConfiguration.initialize(
            context = this,
            defaultNotificationChannel = NOTIFICATION_CHANNEL_ID,
            debug = BuildConfig.DEBUG
        )
    }

    private fun initFirebase() {
        try {
            if (FirebaseApp.getApps(mAppContext).isEmpty()) {
                logger_e("Firebase", "FirebaseApp not initialized!")
                FirebaseApp.initializeApp(mAppContext)
            }

            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.setCrashlyticsCollectionEnabled(true)
        } catch (e: Exception) {
            logger_e("Firebase", "Failed to enable Crashlytics ${e.message}")
        }
//        FirebaseApp.initializeApp(this)
//        logger_d("Firebase", "Initialized: ${FirebaseApp.getInstance().name}")
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
//         Notification Channel 初始化
        KiraFirebaseMessagingService.getDeviceToken()
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }
    }
}
