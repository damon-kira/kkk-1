

package com.kira.ui.core.contract

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class NotificationPermission(
    fragment: Fragment,
    private val onResult: (PermissionResult) -> Unit,
) {

    private val permission: String
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        get() = Manifest.permission.POST_NOTIFICATIONS

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermission = fragment.registerForActivityResult(
        PermissionContract(fragment, permission)
    ) { result ->
        onResult(result)
    }

    fun launch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(permission)
        } else {
            onResult(PermissionResult.GRANTED)
        }
    }
}