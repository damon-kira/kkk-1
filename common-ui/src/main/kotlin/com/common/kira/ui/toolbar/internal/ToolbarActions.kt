

package com.common.kira.ui.toolbar.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
@NonRestartableComposable
internal fun ToolbarActions(
    content: @Composable (RowScope.() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    if (content != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = content,
        )
    }
}