package com.colombia.credit

//import com.facebook.appevents.AppEventsLogger
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.cache.lib.CacheInit
import com.colombia.credit.app.AppEnv
import com.project.util.AesConstant
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LoanApplication: MultiDexApplication() {

    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = this
        CacheInit.get().setContext(this).setDebug(AppEnv.DEBUG)
        initAes()
        ApplicationDelegate.init(this)
//        AppEventsLogger.activateApp(this, getString(R.string.facebook_app_id))
//        PushManagerFactory.init(this)
//        PushManagerFactory.getGaid(this)
//        AdjustManager.init(this, AppEnv.DEBUG)
    }

    private fun initAes() {
        AesConstant.AES_SECRET = Constant.API_SECRET
        AesConstant.apiKey = Constant.API_KEY
        AesConstant.apiIv = Constant.API_IV
    }
}