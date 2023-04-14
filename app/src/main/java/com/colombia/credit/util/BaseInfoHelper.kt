package com.colombia.credit.util

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import com.bigdata.lib.*
import com.bigdata.lib.bean.BaseInfo
import com.cache.lib.SharedPrefUser
import com.colombia.credit.manager.SharedPrefKeyManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.*
import java.util.TimeZone

class BaseInfoHelper {

    fun getBaseInfo(context: Context) {
        val info = BaseInfo()
        info.isSwitchPages = isSwitchPage
        info.readPrivacyAgreementTime = readPrivacyTime
        info.readPrivacyAgreementTimes = readPrivacyCount
        info.registWifi = registWifi
        info.registIp = registIP
        info.faceCheckWifi = faceWifi
        info.loanRequestWifi = loanWifi
        info.internetType = NetWorkUtils.typeToStr(NetWorkUtils.getNetworkState(context))
        info.bootDate =
            ((System.currentTimeMillis() - SystemClock.elapsedRealtime()) / 1000).toString()
        info.bootTime = (SystemClock.elapsedRealtime() / 1000).toString()
        info.batteryMax = BatteryManager.getMaxBattery()
        info.batteryPower = BatteryManager.getLevelBattery()
        info.isRoot = if (RootUtil.isRoot()) 1 else 0
        info.systemVersion = Build.VERSION.SDK_INT.toString()
        info.screenRateLong = DisplayUtils.getRealScreenHeight(context)
        info.screenRateWidth = DisplayUtils.getRealScreenWidth(context)
        info.ocrPhotoExif = ocrExif
        info.faceCheckExif = faceExif
        val imeis = SysUtils.getAllImei(context)
        if (imeis.isNotEmpty()) {
            info.imei = imeis[0]
            info.imei2 = try {
                imeis[1]
            } catch (e: Exception) {
                ""
            }
        }
        info.gaid = GPInfoUtils.getGdid()
        info.imsi = NetWorkUtils.getIMSI(context)
        val androidId = try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            ""
        }
        info.androidId = androidId
        info.mnc = NetWorkUtils.getMnc(context)
        info.storageTotalSize = AppMemoryManager.getDeviceTotalMemory(context)
        info.storageAvailableSize = AppMemoryManager.getDeviceAvailableMemory(context)
        info.sdCardTotalSize = AppMemoryManager.getSdTotalSize()
        info.sdCardAvailableSize = AppMemoryManager.getSdAvaliSize()
//        info.usageTimeBeforeOrder
//        info.firstUseAndRequestIntervalTime
        info.backgroundRecoveryTimes = bgRecoverCount
        info.loginAccountEnterTime = loginTime
        info.chargingStatus = if (PowerConnectionHelper.isCharging(context)) 1 else 0
        info.simState = DevicesHelper.getSimState(context)
        info.timeZone = TimeZone.getDefault().id
        info.vpn = if (NetWorkUtils.isVPN(context)) "0" else "1"
        info.phoneLanguage = SysUtils.getLanguage()
        info.screenBrightness = DevicesHelper.getBrightness(context)
        info.mcc = NetWorkUtils.getMcc(context)
        info.mac = NetWorkUtils.getMac()
        info.developerStatus = SysUtils.isDevelop(context)
        info.addresSimulationApp = 0
        info.operators = NetWorkUtils.getOperator(context)
        info.loanPageStayTime = loanPageStayTime
        info.wifiList = WifiHelper.getWifiInfo(context)
        info.gpsFakeAppList = GsonUtil.toJson(PackageUtil.getGpsMockApp(context))
        info.advertisingId = GPInfoUtils.getGdid()
        info.isAcCharge = PowerConnectionHelper.isAc(context)
        info.isUsbCharge = PowerConnectionHelper.isUsb(context)
        info.audioExternal = -1
        info.audioInternal = -1
        info.downloadFiles = StorageHelper.getDownFileNum()
        info.imagesExternal = -1
        info.imagesInternal = -1
        info.isUsingProxyport = NetWorkUtils.isProxy()
        info.batteryMax = BatteryManager.getMaxBattery()
        info.cpuNum = Runtime.getRuntime().availableProcessors()
        info.wifiMac = WifiHelper.getMac(context)
        info.wifiSsid = WifiHelper.getSSid(context)
        info.dbm = "2dbm"
        info.ramTotalSize = AppMemoryManager.getDeviceTotalMemory(context)
        info.ramUsedSize = AppMemoryManager.getDeviceAvailableMemory(context)
        info.sdkVersion = Build.VERSION.SDK_INT.toString()
        info.iccId = SysUtils.getSimSerial(context)
    }
}

var isSwitchPage: Int
    set(value) = SharedPrefUser.setInt(SharedPrefKeyManager.KEY_CERT_SWITCH_PAGE, value)
    get() = SharedPrefUser.getInt(SharedPrefKeyManager.KEY_CERT_SWITCH_PAGE, 0)

var readPrivacyTime: Long
    set(value) = SharedPrefUser.setLong(SharedPrefKeyManager.KEY_READ_PRIVACY_TIME, value)
    get() = SharedPrefUser.getLong(SharedPrefKeyManager.KEY_READ_PRIVACY_TIME, 0)

var readPrivacyCount: Int
    set(value) {
        if (value < readPrivacyTime) return
        SharedPrefUser.setInt(SharedPrefKeyManager.KEY_READ_PRIVACY_COUNT, value)
    }
    get() = SharedPrefUser.getInt(SharedPrefKeyManager.KEY_READ_PRIVACY_COUNT, 0)

var registWifi: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_REGIS_WIFI, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_REGIS_WIFI, "")

var registIP: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_REGIS_IP, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_REGIS_IP, "")

var faceWifi: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_FACE_WIFI, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_FACE_WIFI, "")

var loanWifi: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_LOAN_WIFI, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_LOAN_WIFI, "")

var ocrExif: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_OCR_EXIF, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_OCR_EXIF, "")
var faceExif: String
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_FACE_EXIF, value)
    get() = SharedPrefUser.getString(SharedPrefKeyManager.KEY_FACE_EXIF, "")

var loanPageStayTime: Long
    set(value) = SharedPrefUser.setLong(SharedPrefKeyManager.KEY_PAGE_STAY_TIME, value)
    get() = SharedPrefUser.getLong(SharedPrefKeyManager.KEY_PAGE_STAY_TIME, 0)

var bgRecoverCount: Int
    set(value) = SharedPrefUser.setInt(SharedPrefKeyManager.KEY_BG_RECOVER_COUNT, value)
    get() = SharedPrefUser.getInt(SharedPrefKeyManager.KEY_BG_RECOVER_COUNT, 0)
var loginTime: Long
    set(value) = SharedPrefUser.setLong(SharedPrefKeyManager.KEY_LOGIN_TIME, value)
    get() = SharedPrefUser.getLong(SharedPrefKeyManager.KEY_LOGIN_TIME, 0)