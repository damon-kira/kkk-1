package com.common.kira.ui.toolbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.common.kira.ui.button.IconButtonSize
import com.common.kira.ui.button.IconButtonSizeDefaults

@Immutable
data class ToolbarSize(
    val height: Dp,
    val shadowSize: Dp,
    val emptyIconPadding: Dp,
    val emptyActionsPadding: Dp,
    val iconButtonSize: IconButtonSize,
    val iconButtonPadding: PaddingValues,
    val contentPadding: PaddingValues,
    val textSpacer: Dp,
)

object ToolbarSizeDefaults {

    val M: ToolbarSize
        get() = ToolbarSize(
            height = 56.dp,
            shadowSize = 4.dp,
            emptyIconPadding = 8.dp,
            emptyActionsPadding = 8.dp,
            iconButtonSize = IconButtonSizeDefaults.L,
            iconButtonPadding = PaddingValues(0.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            textSpacer = 4.dp,
        )
}