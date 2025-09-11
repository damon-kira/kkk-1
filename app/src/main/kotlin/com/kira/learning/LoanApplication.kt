package com.kira.learning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.view.Choreographer
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.cache.lib.CacheInit
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kira.learning.AppEnv.Companion.NOTIFICATION_CHANNEL_ID
import com.kira.learning.messaging.KiraFirebaseMessagingService
import com.kira.quickupload.UploadConfiguration
import com.util.lib.log.logger_e
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security

@HiltAndroidApp
class LoanApplication : MultiDexApplication(), CameraXConfig.Provider {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
        internal var appStartUptime: Long = 0L
    }

    override fun onCreate() {
        appStartUptime = SystemClock.uptimeMillis()
        super.onCreate()
        mAppContext = this

        // 初始化系统错误处理器（放在最前面）
//        SystemErrorHandler.setupGlobalExceptionHandler()

        // 配置延迟任务（Debug 下立即执行便于调试；开启日志）
        DeferredStartup.configure(
            debugImmediate = BuildConfig.DEBUG && false,
            enableLogging = BuildConfig.DEBUG
        )
        // 轻量：本地缓存与 Delegates
        CacheInit.get().setContext(this).setDebug(AppEnv.DEBUG)
        ApplicationDelegate.init(this)

        // 安全相关（需尽早影响后续网络 TLS）
        try {
            Security.insertProviderAt(Conscrypt.newProvider(), 1)
        } catch (_: Throwable) {
        }

        // 注册延迟任务
        registerDeferredTasks()

        // 首帧后调度所有延迟任务（避免阻塞冷启动关键路径）
        Choreographer.getInstance().postFrameCallback { DeferredStartup.dispatchAll(this) }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DeferredStartup.signalLowMemory()
    }

    private fun registerDeferredTasks() {
        // 轻量：Firebase Core / Crashlytics 打开（若需尽早捕获崩溃可放回 onCreate）
        DeferredStartup.register(
            DeferredStartup.Task(
                "FirebaseCore",
                DeferredStartup.Phase.LIGHT
            ) { initFirebaseCore() })
        // 中等：上传模块（创建通知渠道 + SDK 初始化）
        DeferredStartup.register(
            DeferredStartup.Task(
                "UploadInit",
                DeferredStartup.Phase.MEDIUM
            ) { initUpload() })
        // 重：获取 FCM Token（可能触发网络）
        DeferredStartup.register(
            DeferredStartup.Task(
                "FetchFcmToken",
                DeferredStartup.Phase.HEAVY
            ) { fetchFcmToken() })
        // 预留：后续可添加 WebView 预热 / ML 模型下载 等
    }

    private fun initUpload() {
        createNotificationChannel()
        UploadConfiguration.initialize(
            context = this,
            defaultNotificationChannel = NOTIFICATION_CHANNEL_ID,
            debug = BuildConfig.DEBUG
        )
    }

    private fun initFirebaseCore() {
        try {
            if (FirebaseApp.getApps(mAppContext).isEmpty()) {
                FirebaseApp.initializeApp(mAppContext)
            }
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

            // 初始化崩溃报告过滤器
//            CrashReportFilter.setupCrashlyticsFilter()

        } catch (e: Exception) {
            logger_e("Firebase", "Failed FirebaseCore ${e.message}")
        }
    }

    private fun fetchFcmToken() {
        try {
            KiraFirebaseMessagingService.getDeviceToken()
        } catch (e: Exception) {
            logger_e("Firebase", "Fetch token fail ${e.message}")
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }
    }
}
