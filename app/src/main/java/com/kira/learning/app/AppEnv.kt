package com.kira.learning.app

import com.kira.learning.BuildConfig
import com.util.lib.AppUtil

/**
 * Created by weishl on 2023/3/27
 *
 */
class AppEnv {
    companion object {
        var DEBUG = BuildConfig.DEBUG

        val version = AppUtil.getVersionName(getAppContext())
    }

}