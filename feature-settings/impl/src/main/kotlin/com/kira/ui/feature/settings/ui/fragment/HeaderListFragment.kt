

package com.kira.ui.feature.settings.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kira.ui.core.adapter.OnItemClickListener
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.*
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.feature.settings.R
import com.kira.ui.feature.settings.databinding.FragmentHeaderListBinding
import com.kira.ui.feature.settings.ui.adapter.PreferenceAdapter
import com.kira.ui.feature.settings.ui.adapter.PreferenceHeader
import com.kira.ui.feature.settings.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HeaderListFragment : Fragment(R.layout.fragment_header_list) {

    private val viewModel by hiltNavGraphViewModels<SettingsViewModel>(R.id.settings_graph)
    private val binding by viewBinding(FragmentHeaderListBinding::bind)
    private val navController by lazy { findNavController() }

    private lateinit var adapter: PreferenceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFadeTransition(binding.recyclerView, R.id.toolbar)
        postponeEnterTransition(view)
        observeViewModel()

        view.applySystemWindowInsets(true) { _, top, _, bottom ->
            binding.toolbar.updatePadding(top = top)
            binding.root.updatePadding(bottom = bottom)
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = PreferenceAdapter(object : OnItemClickListener<PreferenceHeader> {
            override fun onClick(item: PreferenceHeader) = viewModel.selectHeader(item)
        }).also {
            adapter = it
        }
    }

    private fun observeViewModel() {
        viewModel.headersState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { adapter.submitList(it) }
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