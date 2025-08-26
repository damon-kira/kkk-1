package com.common.kira.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class OutlinedButtonSize(
    val minWidth: Dp,
    val minHeight: Dp,
    val cornerRadius: Dp,
    val borderSize: Dp,
    val innerPadding: PaddingValues,
    val textPadding: PaddingValues,
    val iconSize: Dp,
)

object OutlinedButtonSizeDefaults {

    val S: OutlinedButtonSize
        get() = OutlinedButtonSize(
            minWidth = 64.dp,
            minHeight = 36.dp,
            cornerRadius = 4.dp,
            borderSize = 1.dp,
            innerPadding = PaddingValues(8.dp),
            textPadding = PaddingValues(horizontal = 8.dp),
            iconSize = 20.dp
        )
}