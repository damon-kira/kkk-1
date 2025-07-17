

package com.kira.ui.feature.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.applySystemWindowInsets
import com.kira.ui.core.extensions.postponeEnterTransition
import com.kira.ui.core.extensions.setFadeTransition
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.settings.R
import com.kira.ui.uikit.databinding.LayoutPreferenceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.nio.charset.Charset
import com.kira.ui.uikit.R as UiR

@AndroidEntryPoint
class FilesFragment : PreferenceFragmentCompat() {

    private val binding by viewBinding(LayoutPreferenceBinding::bind)
    private val navController by lazy { findNavController() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_files, rootKey)

        val supportedEncodings = Charset.availableCharsets()
            .map(Map.Entry<String, Charset>::key)
            .toTypedArray()

        findPreference<ListPreference>(SettingsManager.KEY_ENCODING_FOR_OPENING)?.apply {
            entries = supportedEncodings
            entryValues = supportedEncodings
        }
        findPreference<ListPreference>(SettingsManager.KEY_ENCODING_FOR_SAVING)?.apply {
            entries = supportedEncodings
            entryValues = supportedEncodings
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

        binding.toolbar.title = getString(R.string.pref_header_files_title)
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}