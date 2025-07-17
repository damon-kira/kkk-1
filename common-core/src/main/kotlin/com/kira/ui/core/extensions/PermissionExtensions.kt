

package com.kira.ui.core.extensions

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun <T> Context.checkStorageAccess(
    onSuccess: () -> T,
    onFailure: () -> T,
): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            onSuccess()
        } else {
            onFailure()
        }
    } else {
        if (isPermissionGranted(WRITE_EXTERNAL_STORAGE)) {
            onSuccess()
        } else {
            onFailure()
        }
    }
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

fun Activity.shouldShowRequestDialog(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}