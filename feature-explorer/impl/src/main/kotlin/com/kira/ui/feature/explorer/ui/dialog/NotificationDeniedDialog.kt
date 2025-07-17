package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kira.ui.core.extensions.showToast
import com.kira.ui.feature.explorer.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.kira.ui.uikit.R as UiR

@AndroidEntryPoint
class NotificationDeniedDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_notification_permission)
            .setMessage(R.string.dialog_message_notification_permission)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(UiR.string.common_continue) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${requireContext().packageName}")
                    }
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Timber.d(e, e.message)
                    requireContext().showToast(UiR.string.common_error_occurred)
                }
            }
            .create()
    }
}