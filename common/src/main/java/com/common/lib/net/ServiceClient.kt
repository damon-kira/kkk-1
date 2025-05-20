package com.common.lib.net

import android.util.Log
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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
        val certificatePinner = CertificatePinner.Builder()
            .add("*.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAA=") // 伪造指纹
            .build()

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
                this.sslSocketFactory(ProxymanSSLSocketFactory(), TrustAllCerts())
                this.hostnameVerifier { _, _ -> true }
                this.certificatePinner(certificatePinner)
            }
            .build()
        val baseUrl = options.baseUrl
        Log.d(TAG, "provideRetrofit: url = $baseUrl")
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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