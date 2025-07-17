

package com.kira.ui.feature.shortcuts.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.*
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.feature.shortcuts.R
import com.kira.ui.feature.shortcuts.ui.mvi.ShortcutIntent
import com.kira.ui.feature.shortcuts.ui.navigation.ShortcutScreen
import com.kira.ui.feature.shortcuts.ui.viewmodel.ShortcutsViewModel
import com.kira.ui.uikit.databinding.LayoutPreferenceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.kira.ui.uikit.R as UiR

@AndroidEntryPoint
class ShortcutsFragment : PreferenceFragmentCompat() {

    private val viewModel by hiltNavGraphViewModels<ShortcutsViewModel>(R.id.shortcuts_graph)
    private val binding by viewBinding(LayoutPreferenceBinding::bind)
    private val navController by lazy { findNavController() }

    private val defaultMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_shortcuts, menu)
        }
        override fun onPrepareMenu(menu: Menu) = Unit
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.action_restore -> viewModel.obtainEvent(ShortcutIntent.RestoreDefaults)
            }
            return true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_keybindings, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(UiR.layout.layout_preference, container, false).also {
            (it as? ViewGroup)?.addView(
                super.onCreateView(inflater, container, savedInstanceState),
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFadeTransition(binding.root[1] as ViewGroup, UiR.id.toolbar)
        postponeEnterTransition(view)
        observeViewModel()

        view.applySystemWindowInsets(true) { _, top, _, bottom ->
            binding.toolbar.updatePadding(top = top)
            binding.root[1].updatePadding(bottom = bottom)
        }

        binding.toolbar.title = getString(R.string.pref_header_keybindings_title)
        binding.toolbar.addMenuProvider(defaultMenuProvider, viewLifecycleOwner)
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.shortcuts.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { keybindings ->
                keybindings.forEach { model ->
                    val preference = findPreference<Preference>(model.shortcut.key)
                    preference?.setOnPreferenceClickListener {
                        navController.navigate(ShortcutScreen.Edit(model.shortcut.key))
                        true
                    }
                    preference?.summary = StringBuilder().apply {
                        if (model.key == '\u0000') {
                            append(getString(R.string.shortcut_none))
                        } else {
                            if (model.isCtrl) append(getString(UiR.string.common_ctrl) + " + ")
                            if (model.isShift) append(getString(UiR.string.common_shift) + " + ")
                            if (model.isAlt) append(getString(UiR.string.common_alt) + " + ")
                            append(model.key)
                        }
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.viewEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { event ->
                when (event) {
                    is ViewEvent.Toast -> context?.showToast(text = event.message)
                    is ViewEvent.Navigation -> navController.navigate(event.screen)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}