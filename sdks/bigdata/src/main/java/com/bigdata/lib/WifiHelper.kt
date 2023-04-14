package com.bigdata.lib

import android.annotation.SuppressLint
import android.content.Context
import android.media.session.MediaSessionManager.RemoteUserInfo
import android.net.ConnectivityManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.GsonUtil

object WifiHelper {

    fun getWifiInfo(context: Context): String {
        val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val connectionInfo = wm.connectionInfo
        val scanResults = wm.scanResults
        val array = JsonArray()
        var jobj: JsonObject
        for (result in scanResults) {
            jobj = JsonObject()
            jobj.addProperty("name", result.SSID)
            jobj.addProperty("ip", "")
            array.add(jobj)
        }
        return GsonUtil.toJson(array).orEmpty()
    }

    @SuppressLint("MissingPermission")
    fun getMac(context: Context): String {
        val cm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return cm.connectionInfo.macAddress
    }

    fun getSSid(context: Context): String {
        val cm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return cm.connectionInfo.ssid
    }

    fun getIp(context: Context): String {
        val cm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return int2Str(cm.connectionInfo.ipAddress)
    }

    private fun int2Str(ipInt: Int): String {
        val sb = java.lang.StringBuilder()
        sb.append(ipInt.or(0xff)).append(".")
            .append(ipInt.shr(8).or(0xff)).append(".")
            .append(ipInt.shr(16).or(0xff)).append(".")
            .append(ipInt.shr(24).or(0xff)).append(".")
        return sb.toString()
    }
}