package com.bigdata.lib

import android.content.Context
import android.content.pm.PackageManager
import com.bigdata.lib.bean.AppInfo
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.PackageUtil
import com.util.lib.SysUtils
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import com.util.lib.time2Str
import com.util.lib.utcTimeToStr
import java.util.*

/**
 * Created by weishl on 2020/12/31
 *
 */
object DevicesAppHelper {

    private const val TAG = "debug_DevicesAppHelper"

    /**
     * 获取应用列表
     */
    private fun getAppList(): JsonArray {
        val jsonArray = JsonArray()
        try {
            val config = BigDataManager.get().getNetDataListener() ?: return jsonArray
            val appInfors = PackageUtil.getInstallApp(config.getContext())
            val packageManager = config.getContext().packageManager
            if (appInfors != null) {
                var jsonObject: JsonObject
                appInfors.forEach {
                    jsonObject = JsonObject()
                    jsonObject.addProperty("pkgname", it.packageName)//应用包名
                    jsonObject.addProperty(
                        "appname",
                        packageManager.getApplicationLabel(it.applicationInfo).toString()
                    )//app名
                    jsonObject.addProperty(
                        "installtime",
                        time2Str(it.firstInstallTime, locale = Locale.getDefault())
                    )//本地安装时间
                    jsonObject.addProperty(
                        "installtime_utc",
                        utcTimeToStr(it.firstInstallTime)
                    )//utc 安装时间
                    jsonObject.addProperty("timestamps", it.firstInstallTime.toString())//时间戳
                    jsonObject.addProperty(
                        "last_timestamps",
                        it.lastUpdateTime.toString()
                    )//最后安装时间戳
                    jsonObject.addProperty(
                        "type", if (PackageUtil.isPreInstall(it)) {
                            "0"
                        } else {
                            "1"
                        }
                    )//是否预装 0:预装,1:自装
                    jsonArray.add(jsonObject)
//                        if(AppEnv.DEBUG){
//                            Log.i(TAG," jsonObject = $jsonObject")
//                        }
                }
                if (isDebug()) {
                    logger_i(TAG, " applist = $jsonArray")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " applist e = $e")
        }
        return jsonArray
    }

    fun getAppListInfo(context: Context): List<AppInfo> {
        val list = arrayListOf<AppInfo>()
        try {
            val config = BigDataManager.get().getNetDataListener() ?: return list
            val appInfors = PackageUtil.getInstallApp(config.getContext())
            val packageManager = config.getContext().packageManager
            val imei = SysUtils.getDeviceId(context)
            var info: AppInfo
            appInfors.forEach {
                info = AppInfo()
                info.KkXYU = it.packageName
                info.nY9jdxtbN =
                    packageManager.getApplicationLabel(it.applicationInfo).toString() //app名
//                info.kT2wAsVN =
//                    time2Str(it.firstInstallTime, locale = Locale.getDefault()) //本地安装时间
//                info.installtime_utc = utcTimeToStr(it.firstInstallTime) //utc 安装时间
                info.kT2wAsVN = it.firstInstallTime.toString()// 第一次按照时间
                info.k1gCFs = it.lastUpdateTime.toString() // 最后更新时间
                info.BUjkAjl = if (PackageUtil.isPreInstall(it)) 0 else 1
                info.buWMs = it.versionName
                info.uh9U = imei
                list.add(info)
            }
            if (isDebug()) {
                logger_i(TAG, " applist = $list")
            }
        } catch (e: Exception) {
            logger_e(TAG, " applist e = $e")
        }
        return list
    }

    fun getFakeApp(ctx: Context){
        val pm = ctx.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        packages.forEach {appInfo ->
            val packageInfo = pm.getPackageInfo(appInfo.packageName, PackageManager.GET_PERMISSIONS)
            val requestedPermissions = packageInfo.requestedPermissions
        }
    }

}