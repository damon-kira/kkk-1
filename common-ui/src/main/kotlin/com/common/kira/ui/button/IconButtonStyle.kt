package com.common.kira.ui.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.common.kira.ui.SquircleTheme

@Immutable
data class IconButtonStyle(
    val iconColor: Color,
    val disabledIconColor: Color,
)

object IconButtonStyleDefaults {

    val Primary: IconButtonStyle
        @Composable
        @ReadOnlyComposable
        get() = IconButtonStyle(
            iconColor = SquircleTheme.colors.colorTextAndIconPrimary,
            disabledIconColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )

    val Secondary: IconButtonStyle
        @Composable
        @ReadOnlyComposable
        get() = IconButtonStyle(
            iconColor = SquircleTheme.colors.colorTextAndIconSecondary,
            disabledIconColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )
}