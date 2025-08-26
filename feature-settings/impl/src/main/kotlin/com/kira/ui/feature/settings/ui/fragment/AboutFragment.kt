

package com.kira.ui.feature.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.applySystemWindowInsets
import com.kira.ui.core.extensions.navigate
import com.kira.ui.core.extensions.postponeEnterTransition
import com.kira.ui.core.extensions.setFadeTransition
import com.kira.ui.feature.settings.BuildConfig
import com.kira.ui.feature.settings.R
import com.kira.ui.feature.settings.data.utils.applicationName
import com.kira.ui.feature.settings.data.utils.versionCode
import com.kira.ui.feature.settings.data.utils.versionName
import com.kira.ui.feature.settings.ui.navigation.SettingsScreen
import com.common.kira.ui.databinding.LayoutPreferenceBinding
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class AboutFragment : PreferenceFragmentCompat() {

    private val binding by viewBinding(LayoutPreferenceBinding::bind)
    private val navController by lazy { findNavController() }

    private var counter = 1

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_about, rootKey)

        val changelog = findPreference<Preference>(KEY_ABOUT)
        changelog?.title = requireContext().applicationName
        changelog?.summary = getString(
            R.string.pref_about_summary,
            versionName(),
            requireContext().versionCode,
        )
        changelog?.setOnPreferenceClickListener {
            if (counter < 10) {
                counter++
            } else {
                navController.navigate(SettingsScreen.ChangeLog)
            }
            true
        }
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
        setFadeTransition(binding.root[1] as ViewGroup, R.id.toolbar)
        postponeEnterTransition(view)

        view.applySystemWindowInsets(true) { _, top, _, bottom ->
            binding.toolbar.updatePadding(top = top)
            binding.root[1].updatePadding(bottom = bottom)
        }

        binding.toolbar.title = getString(R.string.pref_header_about_title)
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun versionName(): String {
        return if (BuildConfig.DEBUG) {
            requireContext().versionName + getString(R.string.debug_suffix)
        } else {
            requireContext().versionName
        }
    }

    companion object {
        private const val KEY_ABOUT = "ABOUT"
    }
}