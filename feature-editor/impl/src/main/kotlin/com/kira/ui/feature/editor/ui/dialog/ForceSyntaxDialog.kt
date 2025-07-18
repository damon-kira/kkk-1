package com.kira.ui.feature.editor.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.kira.ui.core.extensions.showToast
import com.kira.ui.feature.editor.R
import com.kira.ui.feature.editor.ui.mvi.EditorIntent
import com.kira.ui.feature.editor.ui.viewmodel.EditorViewModel
import com.kira.ui.feature.editor.ui.viewmodel.MiniEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForceSyntaxDialog : DialogFragment() {

    private val viewModel by activityViewModels<EditorViewModel>()
    private val miniEditorViewModel by activityViewModels<MiniEditorViewModel>()
    private val navArgs by navArgs<ForceSyntaxDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val langNames = resources.getStringArray(R.array.language_title)
        val langEntries = resources.getStringArray(R.array.language_name)
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_force_syntax)
            .setSingleChoiceItems(
                langNames,
                langEntries.indexOf(navArgs.languageName)
            ) { _, which ->
                val intent = EditorIntent.ForceSyntaxHighlighting(langEntries[which])
                viewModel.obtainEvent(intent)
                miniEditorViewModel.obtainEvent(intent)
                requireContext().showToast(text = langNames[which])
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }
}