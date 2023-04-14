package com.bigdata.lib.net

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.aes.lib.MD5Utils
import com.bigdata.lib.*
import com.google.gson.JsonObject
import com.util.lib.*
import com.util.lib.BuildConfig
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import com.util.lib.uuid.UUIDHelper
import java.io.BufferedReader
import java.io.FileReader
import java.math.BigDecimal
import java.util.*

/**
 *@author zhujun
 *@description: 大数据网络请求公共参数管理类
 *@date : 2022/3/1 9:45 上午
 */
class BigDataNetBaseParamsManager {

    companion object {

        val dataListener = BigDataManager.get().getNetDataListener()
        private const val TAG = "debug_BigDataNetBaseParamsManager"

        /**
         * 格式化手机号
         */
        fun formatPhone(phoneNumber: String): String {
            return phoneNumber.replace(" ", "")
        }


        /**
         * 格式化单位
         * @param size
         * @return
         */
        fun getFormatSize(size: Double): String {
            val kiloByte = size / 1024
            if (kiloByte < 1) {
                //            return size + "Byte";
                return "0K"
            }

            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K"
            }

            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                val result2 = BigDecimal(java.lang.Double.toString(megaByte))
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M"
            }

            val teraBytes = gigaByte / 1024
            if (teraBytes < 1) {
                val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB"
            }
            val result4 = BigDecimal(teraBytes)
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
        }

        /**
         * @return 0:授权  1:未授权
         */
        fun isPermissionAuth(context: Context, permissionName: String): Int {
            return if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, permissionName)) {
                0
            } else {
                1
            }
        }

        /** 获取权限授权信息 */
        fun getPermissionAuthInfo(): String {
            // 权限授权状态
            return JsonObject().also {
//                it.addProperty("sms", isPermissionAuth(Manifest.permission.READ_SMS))
//                it.addProperty("contact", isPermissionAuth(Manifest.permission.READ_CONTACTS))
//                it.addProperty(
//                    "read_phone_state",
//                    isPermissionAuth(Manifest.permission.READ_PHONE_STATE)
//                )
//                it.addProperty(
//                    "location",
//                    isPermissionAuth(Manifest.permission.ACCESS_FINE_LOCATION)
//                )
//                it.addProperty(
//                    "calendar",
//                    if (isPermissionAuth(Manifest.permission.READ_CALENDAR) == 1 &&
//                        isPermissionAuth(Manifest.permission.WRITE_CALENDAR) == 1
//                    ) 1 else 0
//                )
            }.toString()
        }

        /**
         * md5 imei
         */
        fun md5IMEI(imei: String): String {
            return MD5Utils.getMD5(imei).orEmpty()
        }

        private fun addImeiList(jobj: JsonObject, isMd5: Boolean) {
            if (dataListener == null) {
                logger_e(TAG, "dataListener is null,please set dataListener")
                return
            }
            val allImei = SysUtils.getAllImei(dataListener.getContext())
            if (allImei.isNotEmpty()) {
                allImei.forEachIndexed { index, str ->
                    jobj.addProperty("imei${index + 1}", if (isMd5) md5IMEI(str) else str)
                }
            }
        }

        fun getTotalMemory(): String {
            val str1 = "/proc/meminfo"// 系统内存信息文件
            val str2: String
            val arrayOfString: Array<String>
            var initial_memory: Long = 0

            try {
                val localFileReader = FileReader(str1)
                val localBufferedReader = BufferedReader(
                    localFileReader, 8192
                )
                str2 = localBufferedReader.readLine()// 读取meminfo第一行，系统总内存大小

                arrayOfString =
                    str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                for (num in arrayOfString) {
//                    Log.i(str2, num + "\t")
//                }

                initial_memory = (arrayOfString[1]).toLong() * 1024// 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close()

            } catch (e: Exception) {
            }
            return getFormatSize(initial_memory.toDouble())// Byte转换为KB或者MB，内存大小规格化
        }

        /**
         * 是否是模拟器
         * 现在默认是false
         */
        fun isSimulator(): Boolean {
            var isEmulator = false
            try {
                isEmulator = EmulatorCheckUtil.checkIsRunningInEmulator(
                    dataListener?.getContext(),
                    null
                )
                logger_e(TAG, "是否是模拟器$isEmulator")
            } catch (e: Exception) {
                logger_e(TAG, "$e")
            }
            return isEmulator
        }

        fun getAppVersionCode(): Int {
            val mContext = BigDataManager.get().getNetDataListener()?.getContext()
            return if (mContext != null) {
                PackageUtil.getVersionCode(mContext, mContext.packageName)
            } else {
                1
            }
        }

        /**
         * 获取短信 通话记录 联系人base params
         */
        @SuppressLint("MissingPermission")
        fun getMessageCallLogContactsBaseParams(): JsonObject {
            val jsonObject = JsonObject()
            if (dataListener == null) {
                logger_e(TAG, "dataListener is null,please set dataListener")
                return jsonObject
            }
            try {
                val locationInfo = LocationHelp.getLocationInfo()
                jsonObject.addProperty("os", "android ${Build.VERSION.RELEASE}")//操作系统和版本
                jsonObject.addProperty(
                    "imei",
                    md5IMEI(SysUtils.getDeviceId(dataListener.getContext(), dataListener.getGaid()))
                )
                addImeiList(jsonObject, true)
                jsonObject.addProperty("na", UUIDHelper(dataListener.getContext()).getAesUUid())
                jsonObject.addProperty("uuid", SysUtils.generateCashUUID(dataListener.getContext()))
                //经度
                jsonObject.addProperty("lon", locationInfo?.first.orEmpty())
                //纬度
                jsonObject.addProperty("lat", locationInfo?.second.orEmpty())
                jsonObject.addProperty("mem", getTotalMemory())//手机内存
                jsonObject.addProperty("model", android.os.Build.MODEL)//手机型号
                jsonObject.addProperty("brand", android.os.Build.BRAND)//手机品牌
                jsonObject.addProperty("is_tt", "0")
                jsonObject.addProperty(
                    "is_sim", if (
                        isSimulator()
                    ) {
                        "1"
                    } else {
                        "0"
                    }
                )//是否是模拟器登录
                jsonObject.addProperty("tz", TimeZone.getDefault().id)//时区
                jsonObject.addProperty(
                    "net",
                    NetWorkUtils.getNetworkState(dataListener.getContext())
                )
                jsonObject.addProperty("uptime", System.currentTimeMillis() / 1000)
                jsonObject.addProperty("access_token", dataListener.getUserToken())//token
                jsonObject.addProperty("phone_num", "")//手机号

                jsonObject.addProperty("gaid", dataListener.getGaid())//gaid
                jsonObject.addProperty("ui_version", dataListener.getUIVERSION())//uiveision
                jsonObject.addProperty("version_code", getAppVersionCode())//versioncode
                jsonObject.addProperty("cid", dataListener.getCid())//versioncode
                jsonObject.addProperty("event_id", EventKeyManager.EVENT_ACTIVATION)//事件id
                val screenWH = DisplayUtils.getScreenWH(dataListener.getContext())
                jsonObject.addProperty("width", screenWH[0])
                jsonObject.addProperty("height", screenWH[1])
                jsonObject.addProperty("cpu_num", DisplayUtils.getCpuNum())

                jsonObject.addProperty("battery_level", BatteryManager.getLevelBattery())//剩余电量
                jsonObject.addProperty("battery_max", BatteryManager.getMaxBattery())//最大电量

                jsonObject.addProperty(
                    "total_boot_time",
                    SystemClock.elapsedRealtime().toString()
                )//开机总时长 单位微妙
                jsonObject.addProperty(
                    "total_boot_time_wake",
                    SystemClock.uptimeMillis().toString()
                )//开机总时长 非休眠时间 单位微妙

                jsonObject.addProperty(
                    "app_max_memory",
                    AppMemoryManager.getMaxMemory()
                )//app 可用最大内存
                jsonObject.addProperty(
                    "app_avaliable_memory",
                    AppMemoryManager.getAvaliableMemory()
                )//app 当前可用内存
                jsonObject.addProperty(
                    "app_free_memory",
                    AppMemoryManager.getFreeMemory()
                )//app 可释放内存

                jsonObject.addProperty("manufacturer", Build.MANUFACTURER)//厂商名
                jsonObject.addProperty("product", Build.PRODUCT)//产品名
                jsonObject.addProperty("board", Build.BOARD)//手机主板名
                jsonObject.addProperty("device", Build.DEVICE)//设备名

                val serialNumber = try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        && PermissionChecker.checkSelfPermission(
                            dataListener.getContext(),
                            Manifest.permission.READ_PHONE_STATE
                        ) == PermissionChecker.PERMISSION_GRANTED
                    ) {
                        Build.getSerial()
                    } else {
                        ""
                    }
                } catch (e: Exception) {
                    ""
                }
                jsonObject.addProperty("serialNumber", serialNumber)
                val androidId = try {
                    Settings.Secure.getString(
                        dataListener.getContext().contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        throw e
                    }
                    ""
                }
                jsonObject.addProperty("android_id", androidId)
            } catch (e: Exception) {
                logger_e("debug_netBaseParams", "BaseParams e $e")
            }
            logger_i(TAG, " getMessageCallLogContactsBaseParams = $jsonObject")
            return jsonObject
        }


    }


}