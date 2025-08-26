

package com.common.kira.ui.toolbar.internal

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.common.kira.ui.button.IconButton
import com.common.kira.ui.button.IconButtonStyleDefaults
import com.common.kira.ui.toolbar.ToolbarSize

@Composable
@NonRestartableComposable
internal fun ToolbarIcon(
    @DrawableRes iconRes: Int?,
    contentDescription: String?,
    onNavigationClicked: () -> Unit,
    toolbarSize: ToolbarSize,
    modifier: Modifier = Modifier,
) {
    if (iconRes != null) {
        IconButton(
            iconResId = iconRes,
            iconButtonStyle = IconButtonStyleDefaults.Primary,
            iconButtonSize = toolbarSize.iconButtonSize,
            contentDescription = contentDescription,
            onClick = onNavigationClicked,
            modifier = modifier.padding(toolbarSize.iconButtonPadding),
        )
    }
}