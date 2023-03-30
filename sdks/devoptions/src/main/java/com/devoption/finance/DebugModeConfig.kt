package com.devoption.finance

/**
 * Created by weishl on 2021/7/13
 *
 */
object DebugModeConfig {

    private var apiConfig = ApiConfig()

    private val apiInfo: ApiInfo
        get() = if (DebugApiConfigInfo.isApiDebug) apiConfig.debug.api else apiConfig.release.api

    private val bigDataInfo: ApiInfo
        get() = if (DebugApiConfigInfo.isBigDataDebug) apiConfig.debug.bigData else apiConfig.release.bigData

    val h5Host: String
        get() = if (DebugApiConfigInfo.isH5Debug) apiConfig.debug.h5Host else apiConfig.release.h5Host

    val apiHost: String
        get() = apiInfo.host

    val apiKey: ByteArray
        get() = apiInfo.key

    val apiIv: ByteArray
        get() = apiInfo.iv

    val apiSecret: String
        get() = apiInfo.secret

    val bigDataHost: String
        get() = bigDataInfo.host

    val bigDataKey: ByteArray
        get() = bigDataInfo.key

    val bigDataIv: ByteArray
        get() = bigDataInfo.iv

    val token: String
        get() {
            return DebugSPUtils.getString(DebugSPKey.KEY_TOKEN, "").orEmpty()
        }

    val isApiDebug: Boolean
        get() = DebugApiConfigInfo.isApiDebug

    val isBigDataDebug: Boolean
        get() = DebugApiConfigInfo.isBigDataDebug

    val behaviorIsDB: Boolean
        get()= DebugSPUtils.getBoolean(DebugSPKey.KEY_BEHAVIOR, true)

    internal fun setApiConfig(apiConfig: ApiConfig) {
        DebugModeConfig.apiConfig = apiConfig
    }
}