package com.util.lib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.util.lib.log.isDebug
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import com.util.lib.uuid.UUIDHelper
import com.util.lib.uuid.UUidCheck
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by weisl on 2019/10/15.
 */
object SysUtils {

    private val DEBUG = isDebug()
    private const val TAG = "debug_SysUtils"
    private const val DEVICE_ID_FILENAME_NEW = "DEV2"
    private const val INVALID_IMEI_FILENAME = "non_imei"
    private var sDeviceId: String? = null

    fun getAllImei(context: Context): List<String> {
        if (Build.VERSION.SDK_INT >= 29) { // 无论是否有权限，都会报SecurityException
            return arrayListOf()
        }
        val imeiList = arrayListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val phoneCount = tm.phoneCount
                    for (index in 0 until phoneCount) {
                        val imei = tm.getImei(index)
                        if (imei.isNullOrEmpty()) {
                            continue
                        }
                        logger_e(TAG, "imei $index =============== $imei")
                        imeiList.add(imei)
                    }
                } catch (e: Exception) {

                }
            }
        }
        return imeiList
    }


    @JvmStatic
    @Synchronized
    fun getDeviceId(context: Context, gaid: String?): String {
        if (Build.VERSION.SDK_INT >= 29) { // 无论是否有权限，都会报SecurityException
            // 华为服务关闭的时候返回的都是0
            return if (gaid.isNullOrEmpty() || (gaid == "00000000-0000-0000-0000-000000000000")) {
                generateCashUUID(context)
            } else gaid
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return if (gaid.isNullOrEmpty()) {
                generateCashUUID(context)
            } else gaid
        }
//        if (IMEIDialogHelper.isGetIMEIAgree()) {
        logger_i(TAG, "用户同意抓取imei")
        try {
            // IMEI
            var imei = getImei(context)

            if (UUidCheck.invalidCashDeviceId(context, imei)) {
                logger_i(TAG, "imei 无效")
                return generateCashUUID(context)
            }
            if (TextUtils.isEmpty(imei)) {
                logger_i(TAG, "imei 为空")
                return generateCashUUID(context)
            }
            logger_i(TAG, "imei = $imei")

            // imei不能是0;IMEI必须大于10位
            return if ("0" == imei || imei.length <= 10) {
                generateCashUUID(context)
            } else imei.orEmpty()

        } catch (e: Exception) {
            logger_e(TAG, "获取IMEI e = $e")
        }

        return generateCashUUID(context)
//        } else {
//            logger_i(TAG, "用户不同意抓取imei")
//            return generateCashUUID(context)
//        }
    }


    @SuppressLint("MissingPermission")
    fun getImei(context: Context): String {
        var imei: String? = ""
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return imei.orEmpty()
        }
//
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imei = telephonyManager.deviceId
            if (TextUtils.isEmpty(imei)) { //获取不到时，尝试使用新Api获取
                val imeiList: List<String> = getAllImei(context)
                if (imeiList.isNotEmpty()) {
                    imei = imeiList[0]
                    if (DEBUG) {
                        Log.i(TAG, "获取主卡imei号失败，Android 8.0+ 获取卡一的imei号：$imei")
                    }
                }
            }
        } catch (e: Exception) {
            logger_d(TAG, "exception = $e")
        }
        return imei.orEmpty()
    }

    /**
     * 获取UUID
     */
    @JvmStatic
    @Synchronized
    fun generateCashUUID(context: Context): String {
//        if (sDeviceId == null) {
        val helper = UUIDHelper(context)
        val uuid = if (Looper.getMainLooper() == Looper.myLooper()) {
            helper.getSpUUid()
        } else {
            helper.getUUid()
        }
//        }
        if (DEBUG) {
            Log.d(TAG, "UUID() =$uuid")
        }
        return uuid
    }


    @JvmStatic
    @SuppressLint("MissingPermission")
    fun getPhoneNumber(context: Context): String {
        if (checkReadPhonePermission(context)) return ""
        try {
            val phoneMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = phoneMgr.line1Number.orEmpty()
            if (BuildConfig.DEBUG) {
                logger_d(TAG, "phone number = $phoneNumber")
            }
            return phoneNumber
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, e.toString())
            }
        }
        return ""
    }

    fun getPhoneNumbers(context: Context): ArrayList<String>? {
        if (checkReadPhonePermission(context)) return null
        val arrays = arrayListOf<String>()
        try {
            val phoneMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val sm =
                    context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                val list = sm.activeSubscriptionInfoList
                list.take(2).forEachIndexed { index, item ->
                    val number = getRealMobile(item.number)
                    if (number.isNotEmpty()) {
                        arrays.add(number)
                    }
                }
            } else {
                var phoneNumber = phoneMgr.line1Number.orEmpty()
                phoneNumber = getRealMobile(phoneNumber)
                if (phoneNumber.isNotEmpty()) {
                    arrays.add(phoneNumber)
                }
                try {
                    phoneNumber = phoneMgr.groupIdLevel1
                    phoneNumber = getRealMobile(phoneNumber)
                    if (phoneNumber.isNotEmpty()) {
                        arrays.add(phoneNumber)
                    }
                } catch (e: Exception) {
                    logger_e(TAG, "num2 exception = $e")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, e.toString())
        }
        logger_d(TAG, "phone number = ${arrays.joinToString(",")}")
        return arrays
    }

    private fun getRealMobile(mobile: String): String {
        var temp = mobile
        if (temp.startsWith("+")) {
            temp = temp.substring(1)
        }
        if (!temp.isValidNumber()) {
            return ""
        }
        if (temp.length > 10) {
            temp = temp.substring(temp.length - 10)
        }
        return temp
    }

    fun String.isValidNumber() = Regex("\\d+").matches(this)

    /**
     * 根据包名判断app 是否安装
     *
     * @param packageName 包名
     * @return true：安装  false 未安装
     */
    fun isInstall(context: Context, packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            PackageUtil.getAppPackageInfo(context, packageName)
        } catch (e: Exception) {
            null
        }

        return packageInfo != null
    }

    /**
     * 获取系统语言
     */
    fun getLanguage(): String {
        val locale = Locale.getDefault()
        return java.lang.StringBuilder().append(locale.language).append("-").append(locale.country)
            .toString()
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            result = context.resources.getDimensionPixelOffset(resId)
        }
        return result
    }

    @SuppressLint("HardwareIds")
    fun getAId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            ""
        }
    }

    fun isDevelop(context: Context) : Int{
        return if (Settings.Secure.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) > 0) {
            1
        } else 0
    }

    @SuppressLint("MissingPermission")
    fun getSimSerial(context: Context): String {
        if (checkReadPhonePermission(context)) return ""
        try {
            val phoneMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = phoneMgr.simSerialNumber.orEmpty()
            if (BuildConfig.DEBUG) {
                logger_d(TAG, "phone number = $phoneNumber")
            }
            return phoneNumber
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, e.toString())
            }
        }

        return ""
    }

    private fun checkReadPhonePermission(mContext: Context): Boolean {
        if (PermissionChecker.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_STATE
            ) == PermissionChecker.PERMISSION_DENIED && PermissionChecker.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PermissionChecker.PERMISSION_DENIED
        ) {
            return true
        }
        return false
    }

    /**
     * 是否是模拟器
     * 现在默认是false
     */
    fun isSimulator(context: Context): Boolean {
        var isEmulator = false
        try {
            isEmulator = EmulatorCheckUtil.checkIsRunningInEmulator(
                context,
                null
            )
            logger_e(TAG, "是否是模拟器$isEmulator")
        } catch (e: Exception) {
            logger_e(TAG, "$e")
        }
        return isEmulator
    }

}