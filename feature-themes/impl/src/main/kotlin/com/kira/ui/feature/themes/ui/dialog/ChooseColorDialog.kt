

package com.kira.ui.feature.themes.ui.dialog

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.themes.R
import com.kira.ui.feature.themes.ui.mvi.ThemeIntent
import com.kira.ui.feature.themes.ui.viewmodel.ThemesViewModel
import com.kira.ui.uikit.extensions.ui.uikit.ColorPickerDialog
import com.kira.ui.uikit.extensions.ui.uikit.extensions.toHexString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseColorDialog : ColorPickerDialog() {

    override val titleRes = R.string.dialog_title_color_picker
    override val positiveRes = R.string.action_select
    override val negativeRes = android.R.string.cancel
    override val initialColor: String
        get() = navArgs.value

    private val viewModel by hiltNavGraphViewModels<ThemesViewModel>(R.id.themes_graph)
    private val navArgs by navArgs<ChooseColorDialogArgs>()

    override fun onColorSelected(color: Int) {
        val event = ThemeIntent.ChangeColor(
            key = navArgs.key,
            value = color.toHexString()
        )
        viewModel.obtainEvent(event)
    }
}