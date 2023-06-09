package com.bigdata.lib.net

import com.bigdata.lib.BigDataManager
import com.util.lib.AppUtil
import com.util.lib.SysUtils
import com.util.lib.log.isDebug
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


internal object NetWorkManager {
    val TAG = "debug_bigData_NetWorkManager"


    @JvmStatic
    @Synchronized
    fun synUploadlogMessage(bigDataInfo: String, remoteUrl: String): Response {
        val call = createRequest(bigDataInfo, remoteUrl)
        return call.execute()
    }

    private fun createRequest(bigDataInfo: String, remoteUrl: String): Call {
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            bigDataInfo
        )
        val okHttpClient = OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(mHeaderInterceptor)
            .addInterceptor(httpLog)
            .build()

        val request = Request.Builder().url(remoteUrl)
            .post(body).build()
        return okHttpClient.newCall(request)
    }

    fun asynUplaodMsg(bigDataInfo: String, remoteUrl: String, listener: ((result: Boolean) -> Unit)? = null) {
        createRequest(bigDataInfo, remoteUrl).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener?.invoke(false)
            }

            override fun onResponse(call: Call, response: Response) {
                listener?.invoke(true)
            }
        })
    }


    private val mHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        BigDataManager.get().getNetDataListener()?.also {listener ->
            // app版本
            val ctx = listener.getContext()
            builder.addHeader("vMRdV0dUmj",AppUtil.getVersionCode(ctx, ctx.packageName).toString())// app 版本
            // 设备id
            builder.addHeader("NbBH4GIwmz", SysUtils.getImei(ctx))
            // 客户端类型
            builder.addHeader("dnpiIILLEI", "android")
            // google广告id
            builder.addHeader("pg77Foy4PL", listener.getGaid())
            builder.addHeader("wCxyJuAwkK", listener.getAppToken())// token
            builder.addHeader("kio8YGhwe6", "Xs8jKf5LmN")// 固定值
            builder.addHeader("Content-type","application/json;charset=utf-8")
        }
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