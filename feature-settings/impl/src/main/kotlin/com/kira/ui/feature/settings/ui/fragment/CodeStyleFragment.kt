

package com.kira.ui.feature.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.kira.ui.core.delegate.viewBinding
import com.kira.ui.core.extensions.applySystemWindowInsets
import com.kira.ui.core.extensions.postponeEnterTransition
import com.kira.ui.core.extensions.setFadeTransition
import com.kira.ui.feature.settings.R
import com.common.kira.ui.databinding.LayoutPreferenceBinding
import dagger.hilt.android.AndroidEntryPoint
import com.common.kira.ui.R as UiR

@AndroidEntryPoint
class CodeStyleFragment : PreferenceFragmentCompat() {

    private val binding by viewBinding(LayoutPreferenceBinding::bind)
    private val navController by lazy { findNavController() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_code_style, rootKey)
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

        binding.toolbar.title = getString(R.string.pref_header_codeStyle_title)
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}