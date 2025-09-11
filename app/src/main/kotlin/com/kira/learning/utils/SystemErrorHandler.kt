package com.kira.learning.utils

import android.util.Log

/**
 * 系统错误处理器
 * 用于处理和过滤系统级别的错误，避免干扰应用正常运行
 */
object SystemErrorHandler {

    private const val TAG = "SystemErrorHandler"

    /**
     * 检查是否为系统级蓝牙错误
     */
    fun isBluetoothSystemError(throwable: Throwable?): Boolean {
        val message = throwable?.message ?: return false
        val stackTrace = throwable.stackTraceToString()

        return message.contains("BluetoothActivityEnergyInfo", ignoreCase = true) ||
               stackTrace.contains("BluetoothPowerStatsCollector") ||
               stackTrace.contains("com.android.server.power.stats")
    }

    /**
     * 检查是否为系统级可忽略错误
     */
    fun isSystemLevelError(throwable: Throwable?): Boolean {
        if (throwable == null) return false

        val stackTrace = throwable.stackTraceToString()
        val message = throwable.message ?: ""

        // 系统级蓝牙错误
        if (isBluetoothSystemError(throwable)) return true

        // 其他系统级错误模式
        val systemErrorPatterns = listOf(
            "com.android.server",
            "PowerStatsCollector",
            "ActivityManager",
            "WindowManager"
        )

        return systemErrorPatterns.any { pattern ->
            stackTrace.contains(pattern, ignoreCase = true)
        }
    }

    /**
     * 处理系统级错误
     */
    fun handleSystemError(throwable: Throwable?) {
        if (throwable == null) return

        when {
            isBluetoothSystemError(throwable) -> {
                Log.w(TAG, "系统蓝牙服务错误（可忽略）: ${throwable.message}")
                // 可以在这里添加崩溃统计的过滤逻辑
            }
            isSystemLevelError(throwable) -> {
                Log.w(TAG, "系统级错误（可忽略）: ${throwable.message}")
            }
            else -> {
                Log.e(TAG, "应用级错误", throwable)
            }
        }
    }

    /**
     * 全局异常处理器
     */
    fun setupGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (isSystemLevelError(throwable)) {
                // 系统级错误，记录日志但不崩溃应用
                Log.w(TAG, "拦截系统级错误，避免应用崩溃", throwable)
                return@setDefaultUncaughtExceptionHandler
            }

            // 非系统级错误，交由默认处理器处理
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
