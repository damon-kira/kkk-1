package com.util.lib

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.aes.lib.BuildConfig
import com.cache.lib.SharedPrefGlobal
import java.io.DataInputStream

object AppUtil {

    private val TAG = "debug_AppUtil"

    fun getAppPackageInfo(context: Context, pkgName: String): PackageInfo? {
        try {
            val pm = context.packageManager
            return pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e("AppUtil", "error = $e")
            }
        }

        return null
    }

    fun getVersionCode(context: Context, pkgName: String): Long {
        var versionCode = 1L
        val pkgInfo = getAppPackageInfo(context, pkgName)
        if (pkgInfo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pkgInfo.longVersionCode  // API 28+ 推荐方式
            } else {
                pkgInfo.versionCode.toLong()  // 旧API保持兼容
            }
        }
        return versionCode
    }

    fun getVersionName(context: Context, pkgName: String = context.packageName): String {
        var versionName = ""
        val pkgInfo = getAppPackageInfo(context, pkgName)
        if (pkgInfo != null) {
            versionName = pkgInfo.versionName.toString()
        }
        return versionName
    }

    private var CID = -1
    private val SP_KEY = "key_cid"
    const val CID_DAT = "cid.dat"
    fun getCID(context: Context): Int {
        if (CID < 0) {
            // 先读取Google Play的渠道号
            CID = SharedPrefGlobal.getInt(SP_KEY, -1)
            if (CID > 0) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, " CID from cid: $CID")
                }

                return CID
            }

            // 读取包中的渠道号
            val am = context.assets
            var dis: DataInputStream? = null
            try {
                dis = DataInputStream(am.open(CID_DAT))
                val code = dis.readLine()
                CID = Integer.parseInt(code.trim { it <= ' ' })
                SharedPrefGlobal.setInt(SP_KEY, CID)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, " CID form asset file= $CID")
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "[catched]", e)
                }
            } finally {
                if (dis != null) {
                    try {
                        dis.close()
                    } catch (e: Exception) {
                    }

                }
            }
        }

        return CID
    }
}