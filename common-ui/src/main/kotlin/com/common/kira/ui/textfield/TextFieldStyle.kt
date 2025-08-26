package com.common.kira.ui.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.SquircleTheme

@Immutable
data class TextFieldStyle(
    val backgroundColor: Color,
    val textStyle: TextStyle,
    val textColor: Color,
    val placeholderColor: Color,
    val cursorColor: Color,
    val handleColor: Color,
    val selectionColor: Color,
    val labelTextStyle: TextStyle,
    val labelTextColor: Color,
    val helpTextStyle: TextStyle,
    val helpTextColor: Color,
    val errorTextStyle: TextStyle,
    val errorTextColor: Color,
    val errorBorderColor: Color,
)

object TextFieldStyleDefaults {

    val Default: TextFieldStyle
        @Composable
        @ReadOnlyComposable
        get() = TextFieldStyle(
            backgroundColor = SquircleTheme.colors.colorBackgroundTertiary,
            textStyle = SquircleTheme.typography.text16Regular,
            textColor = SquircleTheme.colors.colorTextAndIconPrimary,
            placeholderColor = SquircleTheme.colors.colorTextAndIconSecondary,
            cursorColor = SquircleTheme.colors.colorPrimary,
            handleColor = SquircleTheme.colors.colorPrimary,
            selectionColor = SquircleTheme.colors.colorPrimary.copy(alpha = 0.4f),
            labelTextStyle = SquircleTheme.typography.text12Regular,
            labelTextColor = SquircleTheme.colors.colorTextAndIconSecondary,
            helpTextStyle = SquircleTheme.typography.text12Regular,
            helpTextColor = SquircleTheme.colors.colorTextAndIconSecondary,
            errorTextStyle = SquircleTheme.typography.text12Regular,
            errorTextColor = SquircleTheme.colors.colorTextAndIconError,
            errorBorderColor = SquircleTheme.colors.colorTextAndIconError,
        )
}