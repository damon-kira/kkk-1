package com.colombia.credit.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.bigdata.lib.bgRecoverCount
import com.cache.lib.SharedPrefUser
import com.colombia.credit.manager.SharedPrefKeyManager
import com.common.lib.base.BaseActivity
import java.lang.ref.WeakReference

object AppInjector {
    private var mCurrActivity: WeakReference<Activity>? = null

    private var appInBackground = false

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            private var activityCounts = 0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                mCurrActivity = WeakReference(activity)
                activityCounts ++
                if (activityCounts == 1){
                    SharedPrefUser.setBoolean(SharedPrefKeyManager.KEY_APP_FRONT_BACK_TAG, true)
                }
                if (appInBackground) {
                    appInBackground = false
                    bgRecoverCount++
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                activityCounts --
                appInBackground = activityCounts == 0
                if (activityCounts == 0){
                    SharedPrefUser.setBoolean(SharedPrefKeyManager.KEY_APP_FRONT_BACK_TAG, false)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun getTopActivity(): BaseActivity? = mCurrActivity?.get() as? BaseActivity
}