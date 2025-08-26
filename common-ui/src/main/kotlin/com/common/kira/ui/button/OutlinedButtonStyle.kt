package com.common.kira.ui.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.SquircleTheme

@Immutable
data class OutlinedButtonStyle(
    val borderColor: Color,
    val textStyle: TextStyle,
    val enabledTextColor: Color,
    val disabledTextColor: Color,
)

object OutlinedButtonStyleDefaults {

    val Primary: OutlinedButtonStyle
        @Composable
        @ReadOnlyComposable
        get() = OutlinedButtonStyle(
            borderColor = SquircleTheme.colors.colorOutline,
            textStyle = SquircleTheme.typography.text14Medium,
            enabledTextColor = SquircleTheme.colors.colorPrimary,
            disabledTextColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )
}