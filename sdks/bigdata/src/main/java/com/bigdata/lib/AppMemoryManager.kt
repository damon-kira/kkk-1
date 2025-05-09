package com.bigdata.lib

import android.app.ActivityManager
import android.app.ActivityManager.MemoryInfo
import android.content.Context
import android.os.Environment
import android.os.StatFs


/**
 * 手机可用内存
 */
object AppMemoryManager {
    /**
     * app 最大可用内存
     */
    fun getMaxMemory(): String {
        val runtime = Runtime.getRuntime()
        return format(runtime.maxMemory())
    }

    /**
     * app 当前可用内存
     */
    fun getAvaliableMemory(): String {
        val runtime = Runtime.getRuntime()
        return format(runtime.totalMemory())
    }

    /**
     * app 可释放内存
     */

    fun getFreeMemory(): String {
        val runtime = Runtime.getRuntime()
        return format(runtime.freeMemory())
    }

    /**
     * 小数点后两位
     */
    private fun format(value: Long): String {
        return "${value}Byte"
    }

    fun getRamTotalMemory(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = MemoryInfo()
        am.getMemoryInfo(memInfo)
        return format(memInfo.totalMem)
    }

    fun getRamAvailableMemory(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = MemoryInfo()
        am.getMemoryInfo(memInfo)
        return format(memInfo.availMem)
    }

    fun getSdTotalSize(): String {
        try {
            val sdkFile = Environment.getExternalStorageDirectory()
            val statFs = StatFs(sdkFile.absolutePath)
            return format(statFs.totalBytes)
        } catch (e: Exception) {
        }
        return "0k"
    }

    fun getSdAvaliSize(): String {
        try {
            val sdkFile = Environment.getExternalStorageDirectory()
            val statFs = StatFs(sdkFile.absolutePath)
            return format(statFs.availableBytes)
        } catch (e: Exception) {
        }
        return "0k"
    }

    fun getDeviceTotal(): String {
        try {
            val path = Environment.getDataDirectory().absolutePath
            val statFs = StatFs(path)
            return format(statFs.totalBytes)
        } catch (e: Exception) {
        }
        return "0k"
    }

    fun getDeviceAvali(): String {
        try {
            val path = Environment.getDataDirectory().absolutePath
            val statFs = StatFs(path)
            return format(statFs.availableBytes)
        } catch (e: Exception) {
        }
        return "0k"
    }
}