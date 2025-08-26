package com.common.kira.ui.dropdown

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DropdownSize(
    val height: Dp,
    val cornerRadius: Dp,
    val startPadding: Dp,
    val endPadding: Dp,
    val textSpacer: Dp,
)

object DropdownSizeDefaults {

    val M: DropdownSize
        get() = DropdownSize(
            height = 42.dp,
            cornerRadius = 4.dp,
            startPadding = 8.dp,
            endPadding = 8.dp,
            textSpacer = 16.dp,
        )
}