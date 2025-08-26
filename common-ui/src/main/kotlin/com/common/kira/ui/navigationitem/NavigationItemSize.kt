package com.common.kira.ui.navigationitem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.common.kira.ui.SquircleTheme

@Immutable
data class NavigationItemSize(
    val itemSize: DpSize,
    val indicatorSize: DpSize,
    val labelSpacer: Dp,
    val labelTextStyle: TextStyle,
)

object NavigationItemSizeDefaults {

    val Default: NavigationItemSize
        @Composable
        @ReadOnlyComposable
        get() = NavigationItemSize(
            itemSize = DpSize(64.dp, 64.dp),
            indicatorSize = DpSize(52.dp, 32.dp),
            labelSpacer = 6.dp,
            labelTextStyle = SquircleTheme.typography.text12Regular,
        )
}