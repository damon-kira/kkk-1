package com.kira.learning.extensions

import androidx.compose.ui.graphics.Color
import com.common.kira.ui.Colors
import com.kira.learning.model.ColorScheme
import com.kira.learning.model.ThemeType

internal fun toComposeColors(colorScheme: ColorScheme?): Colors {
    val defaultTheme = when (colorScheme?.type) {
        ThemeType.LIGHT -> Colors.lightColors()
        ThemeType.DARK -> Colors.darkColors()
        else -> Colors.darkColors()
    }
    if (colorScheme == null) {
        return defaultTheme
    }
    return Colors.dynamicColors(
        colorPrimary = colorScheme.colorPrimary?.let(::Color) ?: defaultTheme.colorPrimary,
        colorOutline = colorScheme.colorOutline?.let(::Color) ?: defaultTheme.colorOutline,
        colorBackgroundPrimary = colorScheme.colorBackgroundPrimary?.let(::Color)
            ?: defaultTheme.colorBackgroundPrimary,
        colorBackgroundSecondary = colorScheme.colorBackgroundSecondary?.let(::Color)
            ?: defaultTheme.colorBackgroundSecondary,
        colorBackgroundTertiary = colorScheme.colorBackgroundTertiary?.let(::Color)
            ?: defaultTheme.colorBackgroundTertiary,
        colorTextAndIconPrimary = colorScheme.colorTextAndIconPrimary?.let(::Color)
            ?: defaultTheme.colorTextAndIconPrimary,
        colorTextAndIconPrimaryInverse = colorScheme.colorTextAndIconPrimaryInverse?.let(::Color)
            ?: defaultTheme.colorTextAndIconPrimaryInverse,
        colorTextAndIconSecondary = colorScheme.colorTextAndIconSecondary?.let(::Color)
            ?: defaultTheme.colorTextAndIconSecondary,
        colorTextAndIconDisabled = colorScheme.colorTextAndIconDisabled?.let(::Color)
            ?: defaultTheme.colorTextAndIconDisabled,
        colorTextAndIconAdditional = colorScheme.colorTextAndIconAdditional?.let(::Color)
            ?: defaultTheme.colorTextAndIconAdditional,
        colorTextAndIconSuccess = colorScheme.colorTextAndIconSuccess?.let(::Color)
            ?: defaultTheme.colorTextAndIconSuccess,
        colorTextAndIconError = colorScheme.colorTextAndIconError?.let(::Color)
            ?: defaultTheme.colorTextAndIconError,
        isDark = colorScheme.type == ThemeType.DARK,
    )
}