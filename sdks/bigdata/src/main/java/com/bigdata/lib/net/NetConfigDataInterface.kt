package com.bigdata.lib.net

import android.content.Context
import com.google.gson.JsonObject


interface NetConfigDataInterface {

    //Context
    fun getContext(): Context

    //gaid
    fun getGaid(): String

    fun isDebug(): Boolean

    //是否处于前台
    fun isAppFront(): Boolean

    //TOKEN
    fun getAppToken():String

    //mBigBaseUrl
    fun getBigUrl():String

    fun addBaseParams(jobj: JsonObject)
}