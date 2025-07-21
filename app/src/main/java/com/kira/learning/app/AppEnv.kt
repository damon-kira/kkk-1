package com.kira.learning.app

import com.kira.learning.BuildConfig
import com.util.lib.AppUtil

class AppEnv {
    companion object {
        var DEBUG = BuildConfig.DEBUG

        const val NOTIFICATION_CHANNEL_ID = "KiraUploadChannel"

        val version = AppUtil.getVersionName(getAppContext())
    }

}