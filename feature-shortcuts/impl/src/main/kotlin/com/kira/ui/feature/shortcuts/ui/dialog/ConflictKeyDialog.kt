

package com.kira.ui.feature.shortcuts.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.kira.ui.feature.shortcuts.R
import com.kira.ui.feature.shortcuts.ui.mvi.ShortcutIntent
import com.kira.ui.feature.shortcuts.ui.viewmodel.ShortcutsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class ConflictKeyDialog : DialogFragment() {

    private val viewModel by hiltNavGraphViewModels<ShortcutsViewModel>(R.id.shortcuts_graph)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(R.string.shortcut_conflict)
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                viewModel.obtainEvent(ShortcutIntent.ResolveConflict(reassign = false))
            }
            .setPositiveButton(UiR.string.common_continue) { _, _ ->
                viewModel.obtainEvent(ShortcutIntent.ResolveConflict(reassign = true))
            }
            .create()
    }
}