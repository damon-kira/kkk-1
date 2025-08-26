package com.common.kira.ui.checkbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.SquircleTheme

@Immutable
data class CheckBoxStyle(
    val checkedColor: Color,
    val uncheckedColor: Color,
    val checkmarkColor: Color,
    val disabledColor: Color,
    val textStyle: TextStyle,
    val enabledTextColor: Color,
    val disabledTextColor: Color,
)

object CheckBoxStyleDefaults {

    val Primary: CheckBoxStyle
        @Composable
        @ReadOnlyComposable
        get() = CheckBoxStyle(
            checkedColor = SquircleTheme.colors.colorPrimary,
            uncheckedColor = SquircleTheme.colors.colorTextAndIconSecondary,
            checkmarkColor = SquircleTheme.colors.colorTextAndIconPrimaryInverse,
            disabledColor = SquircleTheme.colors.colorTextAndIconDisabled,
            textStyle = SquircleTheme.typography.text16Regular,
            enabledTextColor = SquircleTheme.colors.colorTextAndIconPrimary,
            disabledTextColor = SquircleTheme.colors.colorTextAndIconDisabled,
        )
}