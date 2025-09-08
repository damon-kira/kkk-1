package com.common.base.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmExitDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit? All changes will be saved into a cache")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                activity?.finish()
            }
            .create()
    }
}