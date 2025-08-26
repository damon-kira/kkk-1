package com.common.kira.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SquircleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: Colors = if (darkTheme) Colors.darkColors() else Colors.lightColors(),
    applySystemBars: Boolean = true,
    content: @Composable () -> Unit
) {
    val activity = LocalActivity.current
    val view = LocalView.current
    if (applySystemBars) {
        SideEffect {
            val window = activity?.window ?: return@SideEffect
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = !colors.isDark
                isAppearanceLightNavigationBars = !colors.isDark
            }
        }
    }
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides SquircleTheme.typography,
    ) {
        MaterialTheme(colorScheme = colors.toMaterialColors()) {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                content()
            }
        }
    }
}

object SquircleTheme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}