

package com.kira.ui.core.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.kira.ui.core.extensions.isPermissionGranted
import com.kira.ui.core.extensions.shouldShowRequestDialog

class PermissionContract(
    private val fragment: Fragment,
    private val permission: String,
) : ActivityResultContract<String, PermissionResult>() {

    private val contract = ActivityResultContracts.RequestPermission()

    override fun createIntent(context: Context, input: String): Intent {
        return contract.createIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PermissionResult {
        return when {
            contract.parseResult(resultCode, intent) -> PermissionResult.GRANTED
            else -> {
                val showRequestRationale = fragment.activity
                    ?.shouldShowRequestDialog(permission)
                    ?: return PermissionResult.DENIED
                if (!showRequestRationale) {
                    PermissionResult.DENIED_FOREVER
                } else {
                    PermissionResult.DENIED
                }
            }
        }
    }

    override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<PermissionResult>? {
        return if (context.isPermissionGranted(input)) {
            SynchronousResult(PermissionResult.GRANTED)
        } else {
            null
        }
    }
}