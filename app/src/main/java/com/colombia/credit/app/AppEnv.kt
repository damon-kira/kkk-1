package com.colombia.credit.app

import com.colombia.credit.BuildConfig
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