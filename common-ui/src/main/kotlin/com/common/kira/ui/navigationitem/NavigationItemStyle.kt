package com.common.kira.ui.navigationitem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.common.kira.ui.SquircleTheme

@Immutable
data class NavigationItemStyle(
    val selectionColor: Color,
    val iconColorSelected: Color,
    val iconColorUnselected: Color,
    val textColorSelected: Color,
    val textColorUnselected: Color,
    val disabledIconColor: Color,
    val disabledTextColor: Color,
)

object NavigationItemStyleDefaults {

    val Default: NavigationItemStyle
        @Composable
        @ReadOnlyComposable
        get() = NavigationItemStyle(
            selectionColor = SquircleTheme.colors.colorBackgroundTertiary,
            iconColorSelected = SquircleTheme.colors.colorTextAndIconPrimary,
            iconColorUnselected = SquircleTheme.colors.colorTextAndIconSecondary,
            textColorSelected = SquircleTheme.colors.colorTextAndIconPrimary,
            textColorUnselected = SquircleTheme.colors.colorTextAndIconSecondary,
            disabledIconColor = SquircleTheme.colors.colorTextAndIconDisabled,
            disabledTextColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )
}