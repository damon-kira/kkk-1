package com.common.lib.net

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.core.content.PermissionChecker
import com.project.util.AESNormalUtil
import com.aes.lib.MD5Utils
import com.aes.lib.SignatureManager
import com.google.gson.JsonObject
import com.util.lib.*
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import okhttp3.Request
import okhttp3.internal.Version
import java.io.BufferedReader
import java.io.FileReader
import java.math.BigDecimal
import java.util.*

/**
 * Created by sunsg on 2018/1/31.
 * 网络请求公共参数管理类
 */
class NetBaseParamsManager {
    companion object {

        private const val TAG = "debug_NetBaseParamsManager"

        private var mExternalParamsSupplier: ExternalParamsSupplier? = null

        internal fun setExternalParamsSupplier(supplier: ExternalParamsSupplier) {
            mExternalParamsSupplier = supplier
        }

        /**
         * 获取外部参数对象
         */
        private fun getExternalParamsSupplier(): ExternalParamsSupplier {
            val supplier = mExternalParamsSupplier
            return supplier!!
        }

        /**
         * 格式化手机号
         */
        fun formatPhone(phoneNumber: String): String {
            return phoneNumber.replace(" ", "")
        }


        /**
         * 构造data参数
         */
        fun buildDataParmas(jsonObject: JsonObject?, isNeedBase: Boolean): String {
            try {
                //添加必填签名信息
                getMustCashMustParams(jsonObject!!)
                //添加base签名信息
                if (isNeedBase) {
                    getCashNormalBaseParams(jsonObject)
                }
                //2.添加签名json
                jsonObject.addProperty("signature", SignatureManager.mexicoSign(jsonObject))
                val returnData = AESNormalUtil.mexicoEncrypt(jsonObject.toString(), false).orEmpty()
                return returnData
            } catch (e: Exception) {
                if (isDebug()) {
                    logger_e(
                        "debug_BaseRepository",
                        "buildDataParmas error = $e"
                    )
                }
                return ""
            }
        }

        fun getBaseMustParams(): String  {
            return JsonObject().also {jobj ->
                getMustCashMustParams(jobj)
                getCashNormalBaseParams(jobj)
            }.toString()
        }

        /**
         * md5 imei
         */
        fun md5IMEI(imei: String): String {
            return MD5Utils.getMD5(imei).orEmpty()
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

                arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
         * 获取length 随机数
         */
        fun getRandomString(length: Int): String { //length表示生成字符串的长度
            val base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val random = Random()
            val sb = StringBuilder()
            for (i in 0 until length) {
                val number = random.nextInt(base.length)
                sb.append(base[number])
            }
            return sb.toString()
        }

        /**
         * 获取 cash 通用 base params
         */
        @SuppressLint("MissingPermission")
        fun getCashNormalBaseParams(jsonObject: JsonObject) {
            val supplier = getExternalParamsSupplier()
            jsonObject.addProperty("os", "android ${Build.VERSION.RELEASE}")
            jsonObject.addProperty("imei", SysUtils.getDeviceId(supplier.getContext(), supplier.getAdvertisingId()))
            jsonObject.addProperty("imei1", "")
            jsonObject.addProperty("imei2", "")
            addImeiList(jsonObject, false)
            jsonObject.addProperty("uuid", SysUtils.generateCashUUID(supplier.getContext()))
            jsonObject.addProperty("model", android.os.Build.MODEL)
            jsonObject.addProperty("brand", android.os.Build.BRAND)
            val locationInfo = supplier.getLocationInfo()
            jsonObject.addProperty("longitude", locationInfo.longitude.orEmpty())//经度
            jsonObject.addProperty("latitude", locationInfo.latitude.orEmpty())//纬度
            jsonObject.addProperty("time_zone", TimeZone.getDefault().id)//时区
            jsonObject.addProperty("network", NetWorkUtil.getNetworkState(supplier.getContext()))
            jsonObject.addProperty(
                "is_simulator", if (isSimulator()) {
                    "1"
                } else {
                    "0"
                }
            )//是否是模拟器登录
            jsonObject.addProperty("city", "")
            // 优先google服务  0:google 1:华为
//            jsonObject.addProperty("channel_fm", PushManagerFactory.getChannel())
            jsonObject.addProperty("is_use_vpn", if (NetWorkUtil.isVPN(supplier.getContext())) "1" else "0")
        }


        /**
         * 获取cash 必传参数
         */
        fun getMustCashMustParams(jsonObject: JsonObject) {
            val supplier = getExternalParamsSupplier()
            jsonObject.addProperty("app_version", supplier.getAppVersionName())
            jsonObject.addProperty("app_version_code", supplier.getAppVersionCode())

            jsonObject.addProperty("noise", getRandomString(Random().nextInt(16) + 16))//随机串,噪音元素 (16-32)位
            jsonObject.addProperty("request_time", System.currentTimeMillis().toString())
            jsonObject.addProperty("access_token", supplier.getToken())
            jsonObject.addProperty("ui_version", supplier.getUiVersion())
            jsonObject.addProperty("cid", supplier.getChannelId())
            jsonObject.addProperty("is_google_service", if (supplier.isGoogleServiceAvailable()) "1" else "0")
            jsonObject.addProperty("system_language", getLanguage())
            jsonObject.addProperty("platform", "android")
            addThirdSdkIdParams(jsonObject)
        }

        /**
         * 添加第三方ID参数(fcm，appsflyer_id, google_advertising_id)
         */
        private fun addThirdSdkIdParams(jsonObject: JsonObject) {
            val supplier = getExternalParamsSupplier()
            val fcmToken = supplier.getFcmToken()
            jsonObject.addProperty("fcm_token", fcmToken)
            jsonObject.addProperty("instance_id", supplier.getAppInstanceId())
            jsonObject.addProperty("appsflyer_id", supplier.getAppsFlyerUid())
            jsonObject.addProperty("google_advertising_id", supplier.getAdvertisingId())
        }

        /**
         * user-agent
         */
        fun getUserAgent(): String {
            val supplier = getExternalParamsSupplier()
            val ctx = supplier.getContext()
            return "${Version.userAgent()} (Android ${Build.VERSION.RELEASE}) /v/${
                AppUtil.getVersionName(ctx, ctx.packageName)
            }"
        }

        /**
         * 是否是模拟器
         * 现在默认是false
         */
        fun isSimulator(): Boolean {
            val supplier = getExternalParamsSupplier()
            var isEmulator = false
            try {
                isEmulator = EmulatorCheckUtil.checkIsRunningInEmulator(
                    supplier.getContext(),
                    null
                )
                logger_e(TAG, "是否是模拟器$isEmulator")
            } catch (e: Exception) {
                logger_e(TAG, "$e")
            }
            return isEmulator
        }

        /**
         * 获取系统语言
         */
        private fun getLanguage():String {
            val locale = Locale.getDefault()
            return java.lang.StringBuilder().append(locale.language).append("-").append(locale.country).toString()
        }

        //添加header信息
        fun addHeader(builder: Request.Builder) {
            val supplier = getExternalParamsSupplier()
            val context = supplier.getContext()
            builder.addHeader("dfdfd", getUserAgent())// token
            // app版本
            builder.addHeader("dfdfd", supplier.getAppVersionCode().toString())// token
            // 设备id
            builder.addHeader("dfdjf", SysUtils.getImei(supplier.getContext()))
            // 客户端类型
            builder.addHeader("x-platform", "android")
            // google广告id
            builder.addHeader("gaid", supplier.getAdvertisingId())
            //
            //builder.addHeader("App-Language", getLanguage())
//            builder.addHeader("content-type", "application/json:charset=UTF-8")
//            builder.addHeader("User-Agent", getUserAgent())
//            builder.addHeader("x-app-version", supplier.getAppVersionName())
//            builder.addHeader("x-app-version-code", supplier.getAppVersionCode().toString())
//            builder.addHeader("x-sys-version", Build.VERSION.RELEASE)
//            builder.addHeader("x-platform", "android")
//            builder.addHeader("x-device", "${Build.BRAND}${Build.MODEL}")
//            builder.addHeader(
//                "x-client-pixel",
//                "${DisplayUtils.getRealScreenHeight(context)}x" +
//                        "${DisplayUtils.getRealScreenWidth(context)}"
//            )//分辨率
//            builder.addHeader("x-gaid", SysUtils.getDeviceId(context, supplier.getAdvertisingId()))//gaid 现在代表deveice id
//            builder.addHeader("x-channel", supplier.getChannelId())//渠道
////            builder.addHeader("x-utm-source", AppFlyerManager.getAppsFlyerUtmSource())//AppsFlyer返回的渠道名称：例如facebook
//            builder.addHeader("x-appflyer-uid", supplier.getAppsFlyerUid())//appsflyer_uid
//            builder.addHeader("x-app-sign", getSignVersion())//秘钥更换标记
        }

        /** 修改是需要同步 userBehavior/networkManager中的值*/
        fun getSignVersion(): String = "1"


        private fun addImeiList(jobj: JsonObject, isMd5: Boolean) {
            val supplier = getExternalParamsSupplier()
            val context = supplier.getContext()
            val allImei = SysUtils.getAllImei(context)
            if (allImei.isNotEmpty()) {
                allImei.forEachIndexed { index, str ->
                    jobj.addProperty("imei${index + 1}", if (isMd5) md5IMEI(str) else str)
                }
            }
        }

        /** 获取权限授权信息 */
        fun getPermissionAuthInfo(): String {
            // 权限授权状态
            return JsonObject().also {
                it.addProperty("sms", isPermissionAuth(Manifest.permission.READ_SMS))
                it.addProperty("contact", isPermissionAuth(Manifest.permission.READ_CONTACTS))
                it.addProperty("read_phone_state", isPermissionAuth(Manifest.permission.READ_PHONE_STATE))
                it.addProperty("location", isPermissionAuth(Manifest.permission.ACCESS_FINE_LOCATION))
                it.addProperty(
                    "calendar",
                    isPermissionAuth(Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR)
                )
            }.toString()
        }


        /**
         * @return 0:未授权  1:授权
         */
        private fun isPermissionAuth(vararg permissions: String): Int {
            val supplier = getExternalParamsSupplier()
            val context = supplier.getContext()
            val notGrantPermission = permissions.find {
                PermissionChecker.PERMISSION_GRANTED != PermissionChecker.checkSelfPermission(context, it)
            }
            return if(notGrantPermission == null) 1 else 0
        }
    }
}