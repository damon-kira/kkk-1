package com.finance.analysis

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel

object AdjustManager {

    private val ADJUST_TOKEN = "exq67wfmgf0g"

    fun init(application: Application, isDebug: Boolean) {

        val environment = if(isDebug)AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(application, ADJUST_TOKEN, environment, isDebug)
        if (isDebug) {
            config.setLogLevel(LogLevel.INFO)
        }
        Adjust.onCreate(config)

        application.registerActivityLifecycleCallbacks(object:
            Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }
            override fun onActivityStarted(p0: Activity) {
            }
            override fun onActivityResumed(p0: Activity) {
                Adjust.onResume()
            }
            override fun onActivityPaused(p0: Activity) {
                Adjust.onPause()
            }
            override fun onActivityStopped(p0: Activity) {
            }
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }
            override fun onActivityDestroyed(p0: Activity) {
            }
        })
    }
}