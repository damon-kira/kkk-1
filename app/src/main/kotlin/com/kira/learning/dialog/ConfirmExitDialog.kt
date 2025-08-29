package com.kira.learning.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kira.learning.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmExitDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_exit)
            .setMessage(R.string.dialog_message_exit)
            .setNegativeButton(R.string.action_no, null)
            .setPositiveButton(R.string.action_yes) { _, _ ->
                activity?.finish()
            }
            .create()
    }
}