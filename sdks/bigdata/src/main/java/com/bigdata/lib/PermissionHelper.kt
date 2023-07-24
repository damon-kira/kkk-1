package com.bigdata.lib

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

internal object PermissionHelper {

    /**
     * @return 0:授权  1:未授权
     */
    fun isPermissionAuth(context: Context, permissionName: String): Int {
        return if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, permissionName)) {
            0
        } else {
            1
        }
    }
}