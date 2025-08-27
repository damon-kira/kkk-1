package com.kira.ui.feature.settings.data.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

val Context.applicationName: String
    get() = runCatching {
        applicationInfo.loadLabel(packageManager).toString()
    }.getOrDefault("")

val Context.versionName: String
    get() = runCatching {
        packageInfoCompat().versionName.orEmpty()
    }.getOrDefault("")

/**
 * 返回应用版本号 (versionCode)。若失败返回 -1。
 * 使用 longVersionCode (API >=28) 向下兼容旧 versionCode。
 */
val Context.versionCode: Int
    get() = runCatching {
        val pi = packageInfoCompat()
        val longCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pi.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pi.versionCode.toLong()
        }
        longCode.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }.getOrDefault(-1)

// --- 私有兼容辅助 ---
private fun Context.packageInfoCompat(): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
    }