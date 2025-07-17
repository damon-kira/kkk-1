

package com.kira.ui.feature.fonts.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kira.ui.core.contract.ContractResult
import com.kira.ui.core.contract.OpenFileContract
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.*
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.feature.fonts.R
import com.kira.ui.feature.fonts.databinding.FragmentFontsBinding
import com.kira.ui.feature.fonts.domain.model.FontModel
import com.kira.ui.feature.fonts.ui.adapter.FontAdapter
import com.kira.ui.feature.fonts.ui.mvi.FontIntent
import com.kira.ui.feature.fonts.ui.mvi.FontsViewState
import com.kira.ui.feature.fonts.ui.viewmodel.FontsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FontsFragment : Fragment(R.layout.fragment_fonts) {

    private val viewModel by hiltNavGraphViewModels<FontsViewModel>(R.id.fonts_graph)
    private val binding by viewBinding(FragmentFontsBinding::bind)
    private val navController by lazy { findNavController() }
    private val openFileContract = OpenFileContract(this) { result ->
        when (result) {
            is ContractResult.Success -> viewModel.obtainEvent(FontIntent.ImportFont(result.uri))
            is ContractResult.Canceled -> Unit
        }
    }

    private lateinit var adapter: FontAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFadeTransition(binding.recyclerView, R.id.toolbar)
        postponeEnterTransition(view)
        observeViewModel()

        view.applySystemWindowInsets(true) { _, top, _, bottom ->
            binding.toolbar.updatePadding(top = top)
            binding.root.updatePadding(bottom = bottom)
        }

        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            binding.recyclerView.addItemDecoration(it)
        }
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = FontAdapter(object : FontAdapter.Actions {
            override fun selectFont(fontModel: FontModel) {
                viewModel.obtainEvent(FontIntent.SelectFont(fontModel))
            }
            override fun removeFont(fontModel: FontModel) {
                viewModel.obtainEvent(FontIntent.RemoveFont(fontModel))
            }
        }).also {
            adapter = it
        }

        binding.actionAdd.setOnClickListener {
            openFileContract.launch(
                OpenFileContract.OCTET_STREAM,
                OpenFileContract.X_FONT,
                OpenFileContract.FONT,
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_fonts, menu)

                    val searchItem = menu.findItem(R.id.action_search)
                    val searchView = searchItem?.actionView as? SearchView

                    val state = viewModel.fontsState.value
                    if (state.query.isNotEmpty()) {
                        searchItem?.expandActionView()
                        searchView?.setQuery(state.query, false)
                    }

                    searchView?.debounce(viewLifecycleOwner.lifecycleScope) {
                        viewModel.obtainEvent(FontIntent.SearchFonts(it))
                    }
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return false
                }
            },
            viewLifecycleOwner,
        )
    }

    private fun observeViewModel() {
        viewModel.fontsState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                when (state) {
                    is FontsViewState.Empty -> {
                        binding.loadingBar.isVisible = false
                        binding.emptyView.isVisible = true
                        binding.recyclerView.isInvisible = true
                    }
                    is FontsViewState.Data -> {
                        binding.loadingBar.isVisible = false
                        binding.emptyView.isVisible = false
                        binding.recyclerView.isInvisible = false
                        adapter.submitList(state.fonts)
                    }
                    is FontsViewState.Loading -> {
                        binding.loadingBar.isVisible = true
                        binding.emptyView.isVisible = false
                        binding.recyclerView.isInvisible = true
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.viewEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { event ->
                when (event) {
                    is ViewEvent.Toast -> context?.showToast(text = event.message)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}