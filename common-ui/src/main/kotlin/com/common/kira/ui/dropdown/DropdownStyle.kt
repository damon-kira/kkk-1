

package com.common.kira.ui.dropdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.SquircleTheme

@Immutable
data class DropdownStyle(
    val iconColor: Color,
    val textColor: Color,
    val textStyle: TextStyle,
)

object DropdownStyleDefaults {

    val Default: DropdownStyle
        @Composable
        @ReadOnlyComposable
        get() = DropdownStyle(
            iconColor = SquircleTheme.colors.colorTextAndIconPrimary,
            textColor = SquircleTheme.colors.colorTextAndIconPrimary,
            textStyle = SquircleTheme.typography.text16Regular,
        )
}