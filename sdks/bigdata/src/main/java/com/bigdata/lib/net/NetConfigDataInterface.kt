package com.bigdata.lib.net

import android.content.Context

/**
 * Created by zhujun
 */
interface NetConfigDataInterface {

    //Context
    fun getContext(): Context

    //gaid
    fun getGaid(): String

    //UIVERSION
    fun getUIVERSION(): Int

    fun isDebug(): Boolean

    //是否处于前台
    fun isAppFront(): Boolean

    fun getAppAbtest():String

    //TOKEN
    fun getUserToken():String

    //mBigBaseUrl
    fun getBigUrl():String

    //cid
    fun getCid():String

}