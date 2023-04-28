package com.util.lib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresPermission
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

object NetWorkUtils {
    /**
     * 检测网络是否连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val infos = cm.allNetworkInfo
            if (infos != null) {
                for (ni in infos) {
                    if (ni.isConnected) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 检测wifi是否连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }
        }
        return false
    }

    fun isProxy(): Int {
        val proxyHost = System.getProperty("http.proxyHost")
        val proxyPort = System.getProperty("http.proxyPort")
        return if (TextUtils.isEmpty(proxyHost) && TextUtils.isEmpty(proxyPort)) {
            1
        } else 0
    }

    /**
     * 检测3G是否连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun is3gConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
        return false
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isVPN(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }

    /**
     * 判断网址是否有效
     */
    fun isLinkAvailable(link: String?): Boolean {
        val pattern = Pattern.compile(
            "^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(link)
        return matcher.matches()
    }

    //没有网络连接
    private val NETWORN_NONE = 0

    //手机网络数据连接类型
    private val NETWORN_2G = 2
    private val NETWORN_3G = 3
    private val NETWORN_4G = 4

    //wifi连接
    private val NETWORN_WIFI = 5

    //移动网络
    private val NETWORN_MOBILE = 6


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkState(context: Context): Int {
        //获取系统的网络服务
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return NETWORN_NONE

        //如果当前没有网络

        //获取当前网络类型，如果为空，返回无网络
        val activeNetInfo = connManager.activeNetworkInfo
        if (activeNetInfo == null || !activeNetInfo.isAvailable) {
            return NETWORN_NONE
        }

        // 判断是不是连接的是不是wifi
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != wifiInfo) {
            val state = wifiInfo.state
            if (null != state) if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                return NETWORN_WIFI
            }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (null != networkInfo) {
            val state = networkInfo.state
            val strSubTypeName = networkInfo.subtypeName
            if (null != state) if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                return when (activeNetInfo.subtype) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NETWORN_2G
                    TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NETWORN_3G
                    TelephonyManager.NETWORK_TYPE_LTE -> NETWORN_4G
                    else ->                             //中国移动 联通 电信 三种3G制式
                        if (strSubTypeName.equals(
                                "TD-SCDMA",
                                ignoreCase = true
                            ) || strSubTypeName.equals(
                                "WCDMA",
                                ignoreCase = true
                            ) || strSubTypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NETWORN_3G
                        } else {
                            NETWORN_MOBILE
                        }
                }
            }
        }
        return NETWORN_NONE
    }

    fun typeToStr(type: Int): String? {
        var result = ""
        when (type) {
            NETWORN_2G -> {
                result = "2G"
                result = "3G"
                result = "4g"
                result = "wifi"
            }
            NETWORN_3G -> {
                result = "3G"
                result = "4g"
                result = "wifi"
            }
            NETWORN_4G, NETWORN_MOBILE -> {
                result = "4g"
                result = "wifi"
            }
            NETWORN_WIFI -> result = "wifi"
        }
        return result
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getIMSI(context: Context): String? {
        return try {
            val tl = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tl.subscriberId
        } catch (e: Exception) {
            null
        }
    }

    fun getOperator(context: Context): String? {
        return try {
            val tl = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tl.networkOperatorName
        } catch (e: Exception) {
            null
        }
    }

    fun getMnc(context: Context): String {
        val tl = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val str = tl.networkOperator
        return if (str != null && !str.isEmpty()) {
            try {
                str.substring(3)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } else ""
    }

    fun getMcc(context: Context): String {
        val tl = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val str = tl.networkOperator
        return if (str != null && !str.isEmpty()) {
            try {
                str.substring(0, 3)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } else ""
    }

    fun getMac(): String? {
        var netMax: String? = getNetMax()
        if (TextUtils.isEmpty(netMax)) {
            netMax = getMacFromIp()
        }
        return netMax
    }

    private fun getNetMax(): String? {
        try {
            val list = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in list) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val bs = nif.hardwareAddress ?: return null
                val sb = StringBuilder()
                for (b in bs) {
                    sb.append(String.format("%02X", b))
                }
                if (sb.length > 0) {
                    sb.deleteCharAt(sb.length - 1)
                }
                return sb.toString()
            }
        } catch (e: SocketException) {
            throw RuntimeException(e)
        }
        return null
    }

    fun getMacFromIp(): String? {
        var strMac: String? = null
        try {
            val ip = getLocalInetAddr()
            val bytes = NetworkInterface.getByInetAddress(ip).hardwareAddress
            val sb = StringBuffer()
            for (index in bytes.indices) {
                if (bytes[index].toInt() != 0) {
                    sb.append(":")
                }
                val str = Integer.toHexString(bytes[index].toInt() and 0xFF)
                sb.append(if (str.length == 1) "0$str" else str)
            }
            strMac = sb.toString().uppercase(Locale.getDefault())
        } catch (e: Exception) {
        }
        return strMac
    }

    private fun getLocalInetAddr(): InetAddress? {
        var ip: InetAddress? = null
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val workInterface = networkInterfaces.nextElement()
                val inetAddresses = workInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    ip = inetAddresses.nextElement()
                    ip = if (!ip.isLoopbackAddress && ip.hostAddress?.contains(":") != true) {
                        break
                    } else null
                }
                if (ip != null) {
                    break
                }
            }
        } catch (e: Exception) {
        }
        return ip
    }

    private fun hasSim(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val state = tm.simState
        return state != TelephonyManager.SIM_STATE_ABSENT && state != TelephonyManager.SIM_STATE_UNKNOWN
    }
}