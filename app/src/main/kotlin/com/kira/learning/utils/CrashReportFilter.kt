package com.kira.learning.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.util.Log

/**
 * 崩溃报告过滤器
 * 过滤系统级错误，只报告真正的应用问题
 */
object CrashReportFilter {

    private const val TAG = "CrashReportFilter"

    /**
     * 设置Firebase Crashlytics过滤器
     */
    fun setupCrashlyticsFilter() {
        try {
            val crashlytics = FirebaseCrashlytics.getInstance()

            // 设置自定义键值对，用于标识过滤的错误
            crashlytics.setCustomKey("bluetooth_system_errors_filtered", true)
            crashlytics.setCustomKey("filter_version", "1.0")

            Log.d(TAG, "Crashlytics过滤器已启用")
        } catch (e: Exception) {
            Log.w(TAG, "无法设置Crashlytics过滤器: ${e.message}")
        }
    }

    /**
     * 报告错误（经过过滤）
     */
    fun recordException(throwable: Throwable) {
        if (SystemErrorHandler.isSystemLevelError(throwable)) {
            Log.d(TAG, "过滤系统级错误，不上报: ${throwable.message}")
            return
        }

        try {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } catch (e: Exception) {
            Log.w(TAG, "上报崩溃信息失败: ${e.message}")
        }
    }

    /**
     * 记录非致命错误
     */
    fun recordNonFatalException(throwable: Throwable) {
        recordException(throwable)
    }
}
