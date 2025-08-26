

package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.databinding.DialogCreateBinding
import com.kira.ui.feature.explorer.ui.mvi.ExplorerIntent
import com.kira.ui.feature.explorer.ui.viewmodel.ExplorerViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class CreateDialog : DialogFragment() {

    private val viewModel by activityViewModels<ExplorerViewModel>()
    private val navController by lazy { findNavController() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCreateBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_create)
            .setView(binding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_create) { _, _ ->
                val fileName = binding.input.text?.ifEmpty { getString(UiR.string.common_untitled) }
                val isFolder = binding.boxIsFolder.isChecked
                navController.popBackStack()
                viewModel.obtainEvent(ExplorerIntent.CreateFile(fileName.toString(), isFolder))
            }
            .create()
    }
}