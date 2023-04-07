package com.util.lib.net

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.util.lib.log.logger_d

object WifiInfoUtil {

    private val TAG = "WifiHelper"

    @JvmStatic
    fun wifiOpen(context: Context) {
        val wifiManager = getWifiManager(context)
        if (wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
    }

    @JvmStatic
    private fun getWifiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @JvmStatic
    fun startWifiScan(context: Context) {
        getWifiManager(context).startScan()
    }


    @JvmStatic
    fun getConnInfo(context: Context): WifiInfo {
        val info = getWifiManager(context).connectionInfo
        logger_d(TAG, "getConnInfo ssid = ${info.toString()}")
        return info
    }

    fun getWifiName(context: Context) :String {
        val wifiManager = getWifiManager(context)
        if (!wifiManager.isWifiEnabled) return ""
        // 有些设备需要 ACCESS_FINE_LOCATION 权限
        val ssid = wifiManager.connectionInfo.ssid
        logger_d(TAG, "getConnInfo ssid = $ssid")
        return ssid
    }
}