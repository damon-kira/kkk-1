package com.util.lib

import android.os.Build
import java.io.File


object RootUtil {

    private val rootRelatedDirs = arrayOf(
        "/su", "/su/bin/su", "/sbin/su",
        "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su",
        "/system/xbin/su",
        "/system/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su",
        "/system/bin/cufsdosck", "/system/xbin/cufsdosck", "/system/bin/cufsmgr",
        "/system/xbin/cufsmgr", "/system/bin/cufaevdd", "/system/xbin/cufaevdd",
        "/system/bin/conbb", "/system/xbin/conbb"
    )

    @JvmStatic
    fun isRoot(): Boolean {
        var hasRootDir = false
        var rootDirs: Array<String>
        val dirCount = rootRelatedDirs.also { rootDirs = it }.size
        for (i in 0 until dirCount) {
            val dir = rootDirs[i]
            if (File(dir).exists()) {
                hasRootDir = true
                break
            }
        }
        return Build.TAGS != null && Build.TAGS.contains("test-keys") || hasRootDir
    }
}