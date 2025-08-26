

package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.databinding.DialogRenameBinding
import com.kira.ui.feature.explorer.ui.mvi.ExplorerIntent
import com.kira.ui.feature.explorer.ui.viewmodel.ExplorerViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class RenameDialog : DialogFragment() {

    private val viewModel by activityViewModels<ExplorerViewModel>()
    private val navController by lazy { findNavController() }
    private val navArgs by navArgs<RenameDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogRenameBinding.inflate(layoutInflater)
        binding.input.setText(navArgs.fileName)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_rename)
            .setView(binding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_rename) { _, _ ->
                val fileName = binding.input.text?.ifEmpty { getString(UiR.string.common_untitled) }
                navController.popBackStack()
                viewModel.obtainEvent(ExplorerIntent.RenameFile(fileName.toString()))
            }
            .create()
    }
}