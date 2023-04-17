package com.common.lib.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class ServiceClient private constructor() {

    companion object {
        const val TAG = "HttpLog"
        private val INSTANCE = ServiceClient()
        fun getInstance(): ServiceClient = INSTANCE
    }


    var mFailedListener: ApiFailedListener? = null

    fun setGlobalFailedListener(failedListener: ApiFailedListener) {
        mFailedListener = failedListener
    }

    internal fun getGlobalFailedListener(): ApiFailedListener? {
        return mFailedListener
    }

    fun setExternalParamsSupplier(supplier: ExternalParamsSupplier) {
        NetBaseParamsManager.setExternalParamsSupplier(supplier)
    }


    fun <T> createService(options: NetOptions, api: Class<T>): T {
        return provideRetrofit(options).create(api)
    }


    private fun provideRetrofit(options: NetOptions): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .apply {
                if (options.readTimeout > 0) {
                    readTimeout(options.readTimeout, options.timeUnit)
                }
                if (options.writeTimeout > 0) {
                    writeTimeout(options.writeTimeout, options.timeUnit)
                }
                if (options.connectTimeout > 0) {
                    connectTimeout(options.connectTimeout, options.timeUnit)
                }
                options.interceptors.forEach {
                    addInterceptor(it)
                }
            }
            .build()
        val baseUrl = options.baseUrl
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    object SSLSocketClient {
        //获取这个SSLSocketFactory
        val sSLSocketFactory: SSLSocketFactory
            get() = try {
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, getTrustManager(), SecureRandom())
                sslContext.socketFactory
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        //获取TrustManager
        private fun getTrustManager(): Array<TrustManager> {
            return arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
        }
    }
}