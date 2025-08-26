package com.common.kira.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TextButtonSize(
    val minWidth: Dp,
    val minHeight: Dp,
    val cornerRadius: Dp,
    val padding: PaddingValues,
)

object TextButtonSizeDefaults {

    val S: TextButtonSize
        get() = TextButtonSize(
            minWidth = 64.dp,
            minHeight = 36.dp,
            cornerRadius = 4.dp,
            padding = PaddingValues(8.dp),
        )
}