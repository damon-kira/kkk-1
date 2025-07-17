

package com.kira.ui.feature.editor.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kira.ui.feature.editor.R
import com.kira.ui.feature.editor.databinding.DialogGotoLineBinding
import com.kira.ui.feature.editor.ui.mvi.EditorIntent
import com.kira.ui.feature.editor.ui.viewmodel.EditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GotoLineDialog : DialogFragment() {

    private val viewModel by activityViewModels<EditorViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogGotoLineBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_goto_line)
            .setView(binding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_go_to) { _, _ ->
                val line = binding.input.text.toString()
                viewModel.obtainEvent(EditorIntent.GotoLineNumber(line))
            }
            .create()
    }
}