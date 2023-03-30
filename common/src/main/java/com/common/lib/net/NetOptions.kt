package com.common.lib.net

import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 *@author zhujun
 *@description:
 *@date : 2022/9/2 3:47 下午
 */
class NetOptions {


    var readTimeout = 0L
    var writeTimeout = 0L
    var connectTimeout = 0L
    var baseUrl = ""
    var timeUnit = TimeUnit.MILLISECONDS

    var interceptors = ArrayList<Interceptor>()

    fun setReadTimeout(time: Long): NetOptions {
        readTimeout = time
        return this
    }

    fun setWriteTimeout(time: Long): NetOptions {
        writeTimeout = time
        return this
    }

    fun setConnectTimeout(time: Long): NetOptions {
        connectTimeout = time
        return this
    }

    fun setBaseUrl(url: String): NetOptions {
        baseUrl = url
        return this
    }

    fun setTimeUnit(unit: TimeUnit): NetOptions {
        timeUnit = unit
        return this
    }

    fun setInterceptors(interceptorList: ArrayList<Interceptor>): NetOptions {
        interceptors = interceptorList
        return this
    }

}