package com.kira.learning.app

import com.kira.learning.BuildConfig
import com.util.lib.AppUtil

class AppEnv {
    companion object {
        var DEBUG = BuildConfig.DEBUG

        val version = AppUtil.getVersionName(getAppContext())
    }

}