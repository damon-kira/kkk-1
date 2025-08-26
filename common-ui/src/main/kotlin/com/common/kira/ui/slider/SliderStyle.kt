package com.common.kira.ui.slider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.SquircleTheme

@Immutable
data class SliderStyle(
    val thumbColor: Color,
    val trackColor: Color,
    val tickColor: Color,
    val disabledThumbColor: Color,
    val disabledTrackColor: Color,
    val disabledTickColor: Color,
    val textStyle: TextStyle,
    val enabledTextColor: Color,
    val disabledTextColor: Color,
)

object SliderStyleDefaults {

    val Primary: SliderStyle
        @Composable
        @ReadOnlyComposable
        get() = SliderStyle(
            thumbColor = SquircleTheme.colors.colorPrimary,
            trackColor = SquircleTheme.colors.colorPrimary,
            tickColor = SquircleTheme.colors.colorPrimary,
            disabledThumbColor = SquircleTheme.colors.colorTextAndIconDisabled,
            disabledTrackColor = SquircleTheme.colors.colorTextAndIconDisabled,
            disabledTickColor = SquircleTheme.colors.colorTextAndIconDisabled,
            textStyle = SquircleTheme.typography.text16Regular,
            enabledTextColor = SquircleTheme.colors.colorTextAndIconPrimary,
            disabledTextColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )
}