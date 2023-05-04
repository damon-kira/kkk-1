package com.bigdata.lib

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.GsonUtil
import com.util.lib.log.logger_d
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

object WifiHelper {

    fun getWifiInfo(context: Context): String {
        val wm = getWifiManager(context)
//        val connectionInfo = wm.connectionInfo
        val scanResults = wm.scanResults
        val array = JsonArray()
        var jobj: JsonObject
        for (result in scanResults) {
            jobj = JsonObject()
            jobj.addProperty("name", result.SSID)
            jobj.addProperty("ip", "")
            array.add(jobj)
        }
        if (array.size() == 0) {
            return  ""
        }
        return GsonUtil.toJson(array).orEmpty()
    }

    @SuppressLint("MissingPermission")
    fun getMac(context: Context): String {
        try {
            val cm = getWifiManager(context)
            return cm.connectionInfo.macAddress
        } catch (e: Exception) {
        }
        return ""
    }

    fun getSSid(context: Context): String {
        val cm = getWifiManager(context)
        return cm.connectionInfo.ssid
    }

    fun getIp(): String {
        var resultIP = ""
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        var ia: InetAddress? = null
        while (networkInterfaces.hasMoreElements()) {
            val nextElement = networkInterfaces.nextElement()
            val inetAddresses = nextElement.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                ia = inetAddresses.nextElement()
                if (ia is Inet6Address) {
                    continue
                }
                val hostAddress = ia.hostAddress
                logger_d("debug_WifiHelper", "getIp: hostAddress = $hostAddress")
                if (hostAddress != "127.0.0.1") {
                    resultIP = hostAddress
                    break
                }
            }
        }
        return resultIP
    }

    private fun getWifiManager(context: Context) =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private fun int2Str(ipInt: Int): String {
        val sb = java.lang.StringBuilder()
        sb.append(ipInt.or(0xff)).append(".")
            .append(ipInt.shr(8).or(0xff)).append(".")
            .append(ipInt.shr(16).or(0xff)).append(".")
            .append(ipInt.shr(24).or(0xff)).append(".")
        return sb.toString()
    }
}