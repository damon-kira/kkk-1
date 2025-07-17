

package com.kira.ui.core.contract

import android.Manifest
import android.os.Build
import android.os.Environment
import androidx.fragment.app.Fragment

class StoragePermission(
    fragment: Fragment,
    private val onResult: (PermissionResult) -> Unit,
) {

    private val permission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }

    private val requestPermission = fragment.registerForActivityResult(
        PermissionContract(fragment, permission)
    ) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                onResult(PermissionResult.GRANTED)
            } else {
                onResult(PermissionResult.DENIED_FOREVER)
            }
        } else {
            onResult(result)
        }
    }

    fun launch() {
        requestPermission.launch(permission)
    }
}