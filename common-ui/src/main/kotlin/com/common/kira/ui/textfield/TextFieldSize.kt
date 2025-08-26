package com.common.kira.ui.textfield

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TextFieldSize(
    val inputCornerRadius: Dp,
    val inputMinWidth: Dp,
    val inputMinHeight: Dp,
    val inputPadding: PaddingValues,
    val errorBorderSize: Dp,
    val labelPadding: PaddingValues,
    val helpPadding: PaddingValues,
    val errorPadding: PaddingValues,
)

object TextFieldSizeDefaults {

    val M: TextFieldSize
        get() = TextFieldSize(
            inputCornerRadius = 6.dp,
            inputMinWidth = Dp.Unspecified,
            inputMinHeight = 42.dp,
            inputPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            errorBorderSize = 2.dp,
            labelPadding = PaddingValues(bottom = 6.dp),
            helpPadding = PaddingValues(top = 6.dp),
            errorPadding = PaddingValues(top = 6.dp),
        )
}