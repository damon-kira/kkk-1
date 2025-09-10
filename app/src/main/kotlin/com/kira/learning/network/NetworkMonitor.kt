package com.kira.learning.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 网络状态监控工具
 * 提供网络连接状态检测和诊断功能
 */
@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 检查网络是否可用
     */
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        )
    }

    /**
     * 获取网络类型
     */
    fun getNetworkType(): NetworkType {
        val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            else -> NetworkType.OTHER
        }
    }

    /**
     * 检查网络是否按流量计费
     */
    fun isMeteredConnection(): Boolean {
        return connectivityManager.isActiveNetworkMetered
    }
}

enum class NetworkType {
    WIFI, CELLULAR, ETHERNET, OTHER, NONE
}

/**
 * 网络诊断工具
 */
object NetworkDiagnostics {

    /**
     * 生成网络诊断报告
     */
    fun generateDiagnosticInfo(context: Context): NetworkDiagnosticInfo {
        val monitor = NetworkMonitor(context)

        return NetworkDiagnosticInfo(
            isNetworkAvailable = monitor.isNetworkAvailable(),
            networkType = monitor.getNetworkType(),
            isMetered = monitor.isMeteredConnection(),
            timestamp = System.currentTimeMillis()
        )
    }
}

data class NetworkDiagnosticInfo(
    val isNetworkAvailable: Boolean,
    val networkType: NetworkType,
    val isMetered: Boolean,
    val timestamp: Long
)
