

package com.kira.ui.feature.editor.ui.dialog

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import com.kira.ui.feature.editor.R
import com.kira.ui.feature.editor.ui.mvi.EditorIntent
import com.kira.ui.feature.editor.ui.viewmodel.EditorViewModel
import com.kira.ui.uikit.extensions.ui.uikit.ColorPickerDialog
import com.kira.ui.uikit.extensions.ui.uikit.extensions.toHexString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertColorDialog : ColorPickerDialog() {

    override val titleRes = R.string.dialog_title_color_picker
    override val positiveRes = R.string.action_insert
    override val negativeRes = android.R.string.cancel
    override val initialColor = Color.WHITE.toHexString()

    private val viewModel by activityViewModels<EditorViewModel>()

    override fun onColorSelected(color: Int) {
        viewModel.obtainEvent(EditorIntent.InsertColor(color))
    }
}