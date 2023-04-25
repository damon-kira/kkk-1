package com.colombia.credit.net

import android.content.Context
import com.bigdata.lib.LocationHelp
import com.colombia.credit.Constant
import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.app.AppEnv
import com.colombia.credit.app.AppInjector
import com.colombia.credit.expand.getUserToken
import com.colombia.credit.expand.setLogout
import com.colombia.credit.expand.showInvalidDialog
import com.colombia.credit.util.GPInfoUtils
import com.common.lib.net.*
import com.common.lib.net.bean.BaseResponse
import com.common.lib.net.logger.HttpLogInterceptor
import com.common.lib.net.logger.HttpLogger
import com.util.lib.PackageUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by weisl on 2019/8/12.
 */

class ApiManager @Inject constructor() {

    companion object {
        const val TAG = "HttpLog"
        private val INSTANCE = ApiManager()
        fun getInstance(): ApiManager = INSTANCE
        private const val CHECK_TIMEOUT = 4000L
        private const val DEFAULT_TIMEOUT = 15000L
        private const val UPLOAD_TIMEOUT = 45000L

    }

    init {
        ServiceClient.getInstance().setGlobalFailedListener(object : ApiFailedListener {
            override fun onFailed(result: BaseResponse<*>, gotoLogin: Boolean) {
                when {
//                    result.isAppForcedUpdate() -> {
//                        val appUpgradeBean = result.parsingData(AppUpgradeBean::class.java)
//                        ApplicationDelegate.updateLiveData.postValue(appUpgradeBean)
//                    }
                    result.code == ResponseCode.INVALIDTOKEN -> {
                        setLogout()
                        if (gotoLogin) {
                            AppInjector.getTopActivity()?.showInvalidDialog()
                        }
                    }
//                    result.code == ResponseCode.SERVICE_ERROR_CODE -> {
//                        Launch.skipWebViewActivity(
//                            getAppContext(),
//                            H5UrlManager.URL_APP_MAINTAIN
//                        )
//                    }
                }
            }
        })

        ServiceClient.getInstance().setExternalParamsSupplier(object : ExternalParamsSupplier {
            override fun getContext(): Context {
                return getAppContext()
            }

            override fun getUiVersion(): Int {
                return 0
            }

            override fun getAppVersionName(): String {
                return PackageUtil.getVersionName(getAppContext(), getAppContext().packageName)
            }

            override fun getAppVersionCode(): Int {
                return PackageUtil.getVersionCode(getAppContext(), getAppContext().packageName)
            }

            override fun getAdvertisingId(): String {
                return GPInfoUtils.getGdid()
            }

            override fun getChannelId(): String {
                return ""
            }

            override fun getAppsFlyerUid(): String {
                return ""
            }

            override fun getToken(): String {
                return getUserToken()
            }

            override fun isGoogleServiceAvailable(): Boolean {
                return true
            }

            override fun getServiceChannel(): Int {
                return 0
            }

            override fun getFcmToken(): String {
                return GPInfoUtils.getFcmToken()
            }

            override fun getLocationInfo(): Location {
                val locationInfo = LocationHelp.getLocationInfo()
                val location = Location()
                location.longitude = locationInfo?.first.orEmpty()
                location.latitude = locationInfo?.second.orEmpty()
                return location
            }

            override fun getAppInstanceId(): String {
                return /*AppFirebaseInfoHelper.getAppFlyerId()*/""
            }
        })

    }

    private val mApiService by lazy {
        val options = NetOptions()
            .setReadTimeout(DEFAULT_TIMEOUT)
            .setConnectTimeout(DEFAULT_TIMEOUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .setInterceptors(defaultInterceptor)
            .setBaseUrl(BASEURL)
        ServiceClient.getInstance().createService(options, ApiService::class.java)
    }

    private val mDataApiService by lazy {
        val options = NetOptions()
            .setReadTimeout(UPLOAD_TIMEOUT)
            .setConnectTimeout(UPLOAD_TIMEOUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .setInterceptors(defaultInterceptor)
            .setBaseUrl(BASEURL)
        ServiceClient.getInstance().createService(options, DataApiService::class.java)
    }

    /**
     * 上传图片 基础信息不在拦截器中添加
     */
    private val mApiUploadService by lazy {
        val options = NetOptions()
            .setReadTimeout(UPLOAD_TIMEOUT)
            .setConnectTimeout(UPLOAD_TIMEOUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .setInterceptors(notEncryptInterceptor)
            .setBaseUrl(BASEURL)
        ServiceClient.getInstance().createService(options, ApiService::class.java)
    }

    /**
     * 超时时间4s
     */
    private val mCheckService by lazy {
        val options = NetOptions()
            .setReadTimeout(CHECK_TIMEOUT)
            .setConnectTimeout(CHECK_TIMEOUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .setInterceptors(defaultInterceptor)
            .setBaseUrl(BASEURL)
        ServiceClient.getInstance().createService(options, ApiService::class.java)
    }

    /**
     * 超时时间4s
     */
    private val mDownloadService by lazy {
        val options = NetOptions()
            .setReadTimeout(UPLOAD_TIMEOUT)
            .setConnectTimeout(DEFAULT_TIMEOUT)
            .setWriteTimeout(UPLOAD_TIMEOUT)
            .setTimeUnit(TimeUnit.MILLISECONDS)
            .setInterceptors(defaultInterceptor)
            .setBaseUrl(BASEURL)

        ServiceClient.getInstance().createService(options, ApiService::class.java)

    }

    private val mHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        NetBaseParamsManager.addHeader(builder)
        builder.method(original.method(), original.body())
        chain.proceed(builder.build())
    }

    private val logInterceptor =
        if (AppEnv.DEBUG) {
            HttpLogInterceptor(HttpLogger()).apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            null
        }


    //默认拦截器集合
    private val defaultInterceptor = ArrayList<Interceptor>().apply {
//        add(BaseDataAddInterceptor())//必传参数拦截器
//        add(EncryptDecryptInterceptor())//加解密拦截器
        add(mHeaderInterceptor)
        if (logInterceptor != null) {
            add(logInterceptor)
        }
    }

    //无需加密拦截器
    private val notEncryptInterceptor = ArrayList<Interceptor>().apply {
//        add(BaseDataAddInterceptor())
//        add(DecryptInterceptor())//解密拦截器
        add(mHeaderInterceptor)
        if (logInterceptor != null) {
            add(logInterceptor)
        }
    }


    fun createNewOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .apply {
                logInterceptor?.let {
                    addInterceptor(it)
                }
            }
            .build()
    }

    private val BASEURL = Constant.BASE_URL

    private val BASE_H5_URL = Constant.BASE_H5_URL

    fun createApiService(): ApiService = mApiService

    /** 检查是否有密码的接口设置4s超时 */
    fun createDownloadApiService(): ApiService = mDownloadService

    /** 检查是否有密码的接口设置4s超时 */
    fun createCheckApiService(): ApiService = mCheckService

    /** 上传图片45s */
    fun createApiUploadService(): ApiService = mApiUploadService

    // 大数据相关接口
    fun getDataApiService(): DataApiService = mDataApiService

    fun getBaseH5Url(): String = BASE_H5_URL

    fun getBaseUrl() = BASEURL
}