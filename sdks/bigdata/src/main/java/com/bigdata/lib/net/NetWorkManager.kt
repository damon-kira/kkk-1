package com.bigdata.lib.net

import com.bigdata.lib.BigDataManager
import com.bigdata.lib.BigDataSpKeyManager
import com.bigdata.lib.EventKeyManager
import com.bigdata.lib.MCLCManager
import com.cache.lib.SharedPrefUser
import com.util.lib.log.isDebug
import com.util.lib.log.logger_d
import com.util.lib.log.logger_i
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *@author zhujun
 *@description:
 *@date : 2022/3/1 11:57 上午
 */
internal object NetWorkManager {
    val TAG = "debug_bigData_NetWorkManager"


    @JvmStatic
    @Synchronized
    fun uploadlogMessage(bigDataInfo: String, isAllPermission: Boolean) {
        val listener = BigDataManager.get().getNetDataListener() ?: return
        val remoteUrl = listener.getBigUrl()
        if (remoteUrl.isEmpty()) return
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            bigDataInfo
        )

        val okHttpClient = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(mHeaderInterceptor)
            .addInterceptor(httpLog)
            .build()

        val request = Request.Builder().url(remoteUrl)
            .post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logger_d(TAG, "postCashInfo fai ======= ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {

                    response.body()?.string()?.let { body ->
                        logger_i(TAG, "postCashInfo success ======= $body")
                    }
                    MCLCManager.doEvent(EventKeyManager.ConstantDot.EVENT_RESULT_OK)
                    logger_i(TAG, " isAllpermission = $isAllPermission")
                    if (isAllPermission) {
                        logger_i(TAG, "setTime")
                        SharedPrefUser.setLong(
                            BigDataSpKeyManager.CASH_KEY_POST_MCLC,
                            System.currentTimeMillis()
                        )
                    } else {
                        logger_i(TAG, "not settime")
                    }

                }else{

                    MCLCManager.doEvent(EventKeyManager.ConstantDot.EVENT_RESULT_FAILED)
                    logger_i(TAG,"isAllpermission = $isAllPermission postCashInfo error ======= ${response.code()}")

                }
            }

        })



    }



    private val mHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        builder.addHeader("x-app-sign", "1")
        builder.method(original.method(), original.body())
        chain.proceed(builder.build())
    }

    private val httpLog = HttpLoggingInterceptor().apply {
        level = if (isDebug()) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}