package com.common.kira.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.common.kira.ui.SquircleTheme

@Immutable
data class CircularProgressStyle(val color: Color)

object CircularProgressStyleDefaults {

    val Primary: CircularProgressStyle
        @Composable
        @ReadOnlyComposable
        get() = CircularProgressStyle(
            color = SquircleTheme.colors.colorPrimary,
        )

    val Monochrome: CircularProgressStyle
        @Composable
        @ReadOnlyComposable
        get() = CircularProgressStyle(
            color = Color.White,
        )
}