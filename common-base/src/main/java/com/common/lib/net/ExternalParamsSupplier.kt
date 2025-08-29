package com.common.lib.net

import android.content.Context

interface ExternalParamsSupplier {

    fun getContext(): Context

    fun getUiVersion(): Int

    fun getAppVersionName(): String

    fun getAppVersionCode(): Int

    /**
     * 获取渠道id
     */
    fun getChannelId(): String

    /**
     * 获取AppsFlyer的uid
     */
    fun getAppsFlyerUid(): String

    /**
     * 获取应用api token
     */
    fun getToken(): String

    /**
     * Google服务 是否可用
     */
    fun isGoogleServiceAvailable(): Boolean

    /**
     * 服务渠道  google 获取 华为
     */
    fun getServiceChannel(): Int

    fun getLocationInfo(): Location

    fun getAppInstanceId(): String
}

/**
 * 位置信息
 */
class Location {

    /**
     * 经度
     */
    var longitude: String? = null

    /**
     * 维度
     */
    var latitude: String? = null

}