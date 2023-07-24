package com.util.lib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import java.util.*

/**
 * Created by weisl on 2019/10/15.
 */
object SysUtils {

    private val DEBUG = isDebug()
    private const val TAG = "debug_SysUtils"
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

    fun getDeviceId(context: Context): String {
        if (!sDeviceId.isNullOrEmpty()) return sDeviceId.orEmpty()
        var imei = ""
        if (Build.VERSION.SDK_INT < 29) { // 小于29,获取IMEI
            imei = getImei(context)
        }
        if (imei.isEmpty()) { // IMEI没有获取android id
            imei = getAId(context)
        }
        sDeviceId = imei
        return imei
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
     * 获取系统语言
     */
    fun getLanguage(): String {
        val locale = Locale.getDefault()
        return java.lang.StringBuilder().append(locale.language).append("-").append(locale.country)
            .toString()
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
            ) == PermissionChecker.PERMISSION_DENIED/* && PermissionChecker.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PermissionChecker.PERMISSION_DENIED*/
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