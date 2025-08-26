package com.common.kira.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun PreviewBackground(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    SquircleTheme(darkTheme = darkTheme) {
        Surface(color = SquircleTheme.colors.colorBackgroundPrimary) {
            content()
        }
    }
}