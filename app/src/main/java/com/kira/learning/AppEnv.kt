package com.kira.learning

import com.kira.learning.di.getAppContext
import com.util.lib.AppUtil

class AppEnv {
    companion object {
        var DEBUG = BuildConfig.DEBUG

        const val NOTIFICATION_CHANNEL_ID = "KiraUploadChannel"

        val version = AppUtil.getVersionName(getAppContext())
    }

}