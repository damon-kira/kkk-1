package com.common.kira.ui.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class IconButtonSize(
    val iconSize: Dp,
    val rippleSize: Dp,
)

object IconButtonSizeDefaults {

    val XXS: IconButtonSize
        get() = IconButtonSize(
            iconSize = 18.dp,
            rippleSize = 16.dp,
        )

    val XS: IconButtonSize
        get() = IconButtonSize(
            iconSize = 36.dp,
            rippleSize = 18.dp,
        )

    val S: IconButtonSize
        get() = IconButtonSize(
            iconSize = 42.dp,
            rippleSize = 18.dp,
        )

    val M: IconButtonSize
        get() = IconButtonSize(
            iconSize = 48.dp,
            rippleSize = 22.dp,
        )

    val L: IconButtonSize
        get() = IconButtonSize(
            iconSize = 56.dp,
            rippleSize = 24.dp,
        )
}