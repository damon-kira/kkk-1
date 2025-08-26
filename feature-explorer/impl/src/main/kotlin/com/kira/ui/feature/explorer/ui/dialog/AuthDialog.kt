

package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.databinding.DialogAuthBinding
import com.kira.ui.feature.explorer.ui.mvi.ExplorerIntent
import com.kira.ui.feature.explorer.ui.viewmodel.ExplorerViewModel
import com.kira.ui.filesystem.base.model.AuthMethod
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class AuthDialog : DialogFragment() {

    private val viewModel by activityViewModels<ExplorerViewModel>()
    private val navArgs by navArgs<AuthDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAuthBinding.inflate(layoutInflater)
        binding.input.hint = when (AuthMethod.of(navArgs.authMethod)) {
            AuthMethod.PASSWORD -> getString(R.string.hint_enter_password)
            AuthMethod.KEY -> getString(R.string.hint_enter_passphrase)
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_authentication)
            .setView(binding.root)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(UiR.string.common_continue) { _, _ ->
                val password = binding.input.text.toString()
                viewModel.obtainEvent(ExplorerIntent.Authenticate(password))
            }
            .create()
    }
}