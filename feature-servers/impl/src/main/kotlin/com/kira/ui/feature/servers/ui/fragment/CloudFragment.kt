

package com.kira.ui.feature.servers.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.*
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.core.navigation.Screen
import com.kira.ui.feature.servers.R
import com.kira.ui.feature.servers.ui.navigation.ServersScreen
import com.kira.ui.feature.servers.ui.viewmodel.ServersViewModel
import com.common.kira.ui.databinding.LayoutPreferenceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class CloudFragment : PreferenceFragmentCompat() {

    private val viewModel by hiltNavGraphViewModels<ServersViewModel>(R.id.servers_graph)
    private val binding by viewBinding(LayoutPreferenceBinding::bind)
    private val navController by lazy { findNavController() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_cloud, rootKey)
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

        binding.toolbar.title = getString(R.string.pref_header_cloud_title)
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.servers.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { servers ->
                preferenceScreen.removeAll()

                val categoryServers = PreferenceCategory(preferenceScreen.context)
                categoryServers.setTitle(R.string.pref_category_servers)
                preferenceScreen.addPreference(categoryServers)

                val addServer = Preference(preferenceScreen.context)
                addServer.setTitle(R.string.pref_add_server_title)
                addServer.setOnPreferenceClickListener {
                    navController.navigate(Screen.AddServer)
                    true
                }
                categoryServers.addPreference(addServer)

                servers.forEach { serverConfig ->
                    val server = Preference(preferenceScreen.context)
                    server.title = serverConfig.name
                    server.summary = serverConfig.address
                    server.setOnPreferenceClickListener {
                        navController.navigate(ServersScreen.EditServer(serverConfig))
                        true
                    }
                    categoryServers.addPreference(server)
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