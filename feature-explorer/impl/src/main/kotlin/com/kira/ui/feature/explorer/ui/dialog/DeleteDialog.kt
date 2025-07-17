package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.ui.mvi.ExplorerIntent
import com.kira.ui.feature.explorer.ui.viewmodel.ExplorerViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.kira.ui.uikit.R as UiR

@AndroidEntryPoint
class DeleteDialog : DialogFragment() {

    private val viewModel by activityViewModels<ExplorerViewModel>()
    private val navController by lazy { findNavController() }
    private val navArgs by navArgs<DeleteDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val isMultiDelete = navArgs.fileCount > 1

        val dialogTitle = if (isMultiDelete) {
            getString(R.string.dialog_title_multi_delete)
        } else {
            navArgs.fileName
        }

        val dialogMessage = if (isMultiDelete) {
            R.string.dialog_message_multi_delete
        } else {
            R.string.dialog_message_delete
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(UiR.string.common_delete) { _, _ ->
                navController.popBackStack()
                viewModel.obtainEvent(ExplorerIntent.DeleteFile)
            }
            .create()
    }
}