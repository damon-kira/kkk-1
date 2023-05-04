package com.finance.analysis

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.util.lib.SysUtils

/**
 * Created by weishl on 2022/11/25
 *
 */
/** 是否有google服务 */
fun googleIsAvailable(context: Context): Boolean {
    try {
        val instance = GoogleApiAvailability.getInstance()
        return instance.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS &&
                SysUtils.isInstall(context, "com.android.vending")
    } catch (e: Exception) {

    }

    return false
}