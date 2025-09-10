package com.kira.learning.network

/**
 * 网络配置管理类
 * 统一管理网络相关的配置参数
 */
object NetworkConfig {
    // 超时配置 (秒)
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // 重试配置
    const val MAX_RETRY_COUNT = 2
    const val RETRY_DELAY_MS = 1000L

    // 缓存配置
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB
    const val CACHE_MAX_AGE = 60 * 5 // 5分钟
    const val CACHE_MAX_STALE = 60 * 60 * 24 * 7 // 7天

    // 日志配置
    const val ENABLE_LOGGING = true

    // 请求头配置
    const val USER_AGENT = "KiraApp/1.0 (Android)"
    const val ACCEPT_TYPE = "application/json"
    const val CONTENT_TYPE = "application/json"
}
