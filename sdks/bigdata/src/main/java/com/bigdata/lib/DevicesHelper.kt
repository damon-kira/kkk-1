package com.bigdata.lib

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.bigdata.lib.bean.DeviceInfo
import com.util.lib.SysUtils
import java.net.NetworkInterface
import java.util.Collections

object DevicesHelper {

    fun getDevicesInfo(context: Context): DeviceInfo {
        return DeviceInfo().also {
            it.l9uzoD39Q8 = Build.MODEL
            it.TAEgE = Build.BRAND
            it.OCwx = SysUtils.getImei(context)
            val locationInfo = LocationHelp.getLocationInfo()
            it.uXBlXBew = locationInfo?.first ?: "0"
            it.u3zNZE = locationInfo?.second ?: "0"
        }
    }

    fun getSimState(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (tm.simState == TelephonyManager.SIM_STATE_ABSENT) {
            return 1
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (tm.activeModemCount == 1) {
                    return 2
                } else 3
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return if (tm.phoneCount == 1) 2 else 3
            }
        }
        return 1
    }

    fun isVPN(): Boolean {
        val all = Collections.list(NetworkInterface.getNetworkInterfaces())
        var result = false
        for (item in all) {
            if (item.name == "tun0" || item.name == "ppp0") {
                result = true
                break
            }
        }
        return result
    }

    fun getBrightness(context: Context): Int {
        val cr = context.contentResolver
        try {
            return Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
        }
        return 0
    }

    fun getAudioNum(context: Context){
        val ams = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    }
}