package com.bigdata.lib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import com.bigdata.lib.*
import com.bigdata.lib.bean.BaseInfo
import com.cache.lib.SharedPrefUser
import com.google.gson.JsonObject
import com.util.lib.*
import java.util.*

object BaseInfoHelper {

    @SuppressLint("MissingPermission")
    fun getBaseInfo(context: Context): JsonObject {
        val info = BaseInfo()
        info.isSwitchPages = isSwitchPage.toString()
        info.readPrivacyAgreementTime = readPrivacyTime.toString()
        info.readPrivacyAgreementTimes = readPrivacyCount.toString()
        info.registWifi = registWifi
        info.registIp = registIP
        info.faceCheckWifi = faceWifi
        info.loanRequestWifi = loanWifi
        info.internetType = NetWorkUtils.typeToStr(NetWorkUtils.getNetworkState(context))
        info.bootDate =
            ((System.currentTimeMillis() - SystemClock.elapsedRealtime()) / 1000).toString()
        info.bootTime = (SystemClock.elapsedRealtime() / 1000).toString()
        info.batteryMax = BatteryManager.getMaxBattery().toString()
        info.batteryPower = BatteryManager.getLevelBattery().toString()
        info.isRoot = if (RootUtil.isRoot()) "1" else "0"
        info.systemVersion = Build.VERSION.CODENAME
        info.screenRateLong = DisplayUtils.getRealScreenHeight(context).toString()
        info.screenRateWidth = DisplayUtils.getRealScreenWidth(context).toString()
//        info.ocrPhotoExif = ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FACE)
//        info.faceCheckExif = ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FACE)
        val imeis = SysUtils.getAllImei(context)
        if (imeis.isNotEmpty()) {
            info.imei = imeis[0]
            info.imei2 = try {
                imeis[1]
            } catch (e: Exception) {
                ""
            }
        }
        val gaid = BigDataManager.get().getNetDataListener()?.getGaid().orEmpty()
        info.gaid = gaid
        info.advertisingId = gaid
        info.imsi = NetWorkUtils.getIMSI(context)
        val androidId = try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            ""
        }
        info.androidId = androidId
        info.mnc = NetWorkUtils.getMnc(context)
        info.storageTotalSize = AppMemoryManager.getDeviceTotal()
        info.storageAvailableSize = AppMemoryManager.getDeviceAvali()
        info.sdCardTotalSize = AppMemoryManager.getSdTotalSize()
        info.sdCardAvailableSize = AppMemoryManager.getSdAvaliSize()
        info.usageTimeBeforeOrder = "0"
        info.firstUseAndRequestIntervalTime = "0"
        info.backgroundRecoveryTimes = bgRecoverCount.toString()
        info.loginAccountEnterTime = loginTime.toString()
        info.chargingStatus = if (PowerConnectionHelper.isCharging(context)) "1" else "0"
        info.simState = DevicesHelper.getSimState(context).toString()
        info.timeZone = TimeZone.getDefault().id
        info.vpn = if (NetWorkUtils.isVPN(context)) "0" else "1"
        info.phoneLanguage = SysUtils.getLanguage()
        info.screenBrightness = DevicesHelper.getBrightness(context)
        info.mcc = NetWorkUtils.getMcc(context)
        info.mac = NetWorkUtils.getMac()
        info.developerStatus = SysUtils.isDevelop(context)
        info.addresSimulationApp = if (SysUtils.isSimulator(context)) 1 else 0
        info.operators = NetWorkUtils.getOperator(context)
        info.loanPageStayTime = loanPageStayTime
        info.wifiList = WifiHelper.getWifiInfo(context)
        val gps = PackageUtil.getGpsMockApp(context)
        val gpsResult = if (gps.size() == 0) {
            ""
        } else GsonUtil.toJson(gps).orEmpty()
        info.gpsFakeAppList = gpsResult
        val images = StorageHelper.getImageNum(context)
        info.photoAlbumListUrl = /*images.joinToString(",")*/""
        info.isAcCharge = PowerConnectionHelper.isAc(context).toString()
        info.isUsbCharge = PowerConnectionHelper.isUsb(context).toString()
        info.audioExternal = "-1"
        info.audioInternal = "-1"
        info.downloadFiles = StorageHelper.getDownFileNum().toString()

        info.imagesExternal = images.size.toString()
        info.imagesInternal = "-1"
        info.isUsingProxyport = NetWorkUtils.isProxy().toString()
        info.batteryMax = BatteryManager.getMaxBattery().toString()
        info.cpuNum = Runtime.getRuntime().availableProcessors().toString()
        info.wifiMac = WifiHelper.getMac(context)
        info.wifiSsid = WifiHelper.getSSid(context)
        info.dbm = "2dbm"
        info.ramTotalSize = AppMemoryManager.getRamTotalMemory(context)
        info.ramUsedSize = AppMemoryManager.getRamAvailableMemory(context)
        info.sdkVersion = Build.VERSION.SDK_INT.toString()
        info.iccId = SysUtils.getSimSerial(context)
        val jobj = GsonUtil.toJsonObject(info)?.apply {
            BigDataManager.get().getNetDataListener()?.addBaseParams(this)
        }
        return jobj ?: JsonObject()
    }
}

var isSwitchPage: Int
    set(value) = SharedPrefUser.setInt(SpKeyManager.KEY_CERT_SWITCH_PAGE, value)
    get() = SharedPrefUser.getInt(SpKeyManager.KEY_CERT_SWITCH_PAGE, 0)

var readPrivacyTime: Long
    set(value) = SharedPrefUser.setLong(SpKeyManager.KEY_READ_PRIVACY_TIME, value)
    get() = SharedPrefUser.getLong(SpKeyManager.KEY_READ_PRIVACY_TIME, 0)

var readPrivacyCount: Int
    set(value) {
        if (value < readPrivacyTime) return
        SharedPrefUser.setInt(SpKeyManager.KEY_READ_PRIVACY_COUNT, value)
    }
    get() = SharedPrefUser.getInt(SpKeyManager.KEY_READ_PRIVACY_COUNT, 0)

var registWifi: String
    set(value) = SharedPrefUser.setString(SpKeyManager.KEY_REGIS_WIFI, value)
    get() = SharedPrefUser.getString(SpKeyManager.KEY_REGIS_WIFI, "")

var registIP: String
    set(value) = SharedPrefUser.setString(SpKeyManager.KEY_REGIS_IP, value)
    get() = SharedPrefUser.getString(SpKeyManager.KEY_REGIS_IP, "")

var faceWifi: String
    set(value) = SharedPrefUser.setString(SpKeyManager.KEY_FACE_WIFI, value)
    get() = SharedPrefUser.getString(SpKeyManager.KEY_FACE_WIFI, "")

var loanWifi: String
    set(value) = SharedPrefUser.setString(SpKeyManager.KEY_LOAN_WIFI, value)
    get() = SharedPrefUser.getString(SpKeyManager.KEY_LOAN_WIFI, "")

var loanPageStayTime: Long
    set(value) = SharedPrefUser.setLong(SpKeyManager.KEY_PAGE_STAY_TIME, value)
    get() = SharedPrefUser.getLong(SpKeyManager.KEY_PAGE_STAY_TIME, 0)

var bgRecoverCount: Int
    set(value) = SharedPrefUser.setInt(SpKeyManager.KEY_BG_RECOVER_COUNT, value)
    get() = SharedPrefUser.getInt(SpKeyManager.KEY_BG_RECOVER_COUNT, -1)
var loginTime: Long
    set(value) = SharedPrefUser.setLong(SpKeyManager.KEY_LOGIN_TIME, value)
    get() = SharedPrefUser.getLong(SpKeyManager.KEY_LOGIN_TIME, 0)