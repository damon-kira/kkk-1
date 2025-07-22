package com.kira.ui.feature.editor.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.editor.R
import com.kira.ui.feature.editor.ui.mvi.EditorIntent
import com.kira.ui.feature.editor.ui.viewmodel.EditorViewModel
import com.kira.ui.feature.editor.ui.viewmodel.MiniEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CloseModifiedDialog : DialogFragment() {

    private val viewModel by activityViewModels<EditorViewModel>()
    private val miniEditorViewModel by activityViewModels<MiniEditorViewModel>()
    private val navArgs by navArgs<CloseModifiedDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(navArgs.fileName)
            .setMessage(R.string.dialog_message_close_tab)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_close) { _, _ ->
                viewModel.obtainEvent(EditorIntent.CloseTab(navArgs.position, true))
                miniEditorViewModel.obtainEvent(EditorIntent.CloseTab(navArgs.position, true))
            }
            .create()
    }
}