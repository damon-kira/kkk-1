package com.colombia.credit

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.bigdata.lib.BigDataManager
import com.bigdata.lib.net.NetConfigDataInterface
import com.cache.lib.CacheInit
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.AppEnv
import com.colombia.credit.app.AppInjector
import com.colombia.credit.expand.getUserToken
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.webview.WebViewPool
import com.colombia.credit.util.GPInfoUtils
import com.colombia.credit.util.ImageInfoUtil
import com.google.gson.JsonObject
import com.project.util.AesConstant
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LoanApplication: Application() {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this
        CacheInit.get().setContext(this)
        AppInjector.init(this)
        WebViewPool.INSTANCE.init(this, false)
        AesConstant.AES_SECRET = Constant.API_SECRET
        AesConstant.apiKey = Constant.API_KEY
        AesConstant.apiIv = Constant.API_IV

        initBigData()
    }

    private fun initBigData(){
        BigDataManager.get().setNetDataListener(object: NetConfigDataInterface{
            override fun getContext(): Context = getAppContext()

            override fun getGaid(): String = GPInfoUtils.getGdid()

            override fun isDebug(): Boolean = AppEnv.DEBUG

            override fun isAppFront(): Boolean = SharedPrefUser.getBoolean(SharedPrefKeyManager.KEY_APP_FRONT_BACK_TAG, false)

            override fun getAppToken(): String = getUserToken()

            override fun getBigUrl(): String = Constant.BIG_DATA_URL

            override fun addBaseParams(jobj: JsonObject) {
                // ocrPhotoExif
                jobj.addProperty("p4yg", ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FRONT))
                // faceExif
                jobj.addProperty("Bgp3rnTyWw", ImageInfoUtil.getInfo(SharedPrefKeyManager.KEY_IMAGE_FACE))
            }
        })
    }
}