

package com.common.kira.ui.toolbar.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.toolbar.ToolbarSize

@Composable
internal fun ToolbarContent(
    title: String?,
    subtitle: String?,
    alignment: Alignment.Horizontal,
    toolbarSize: ToolbarSize,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = alignment,
        modifier = modifier
            .padding(toolbarSize.contentPadding)
            .semantics(mergeDescendants = true) {
                heading()
            },
    ) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                style = SquircleTheme.typography.text18Medium,
                color = SquircleTheme.colors.colorTextAndIconPrimary,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        if (!subtitle.isNullOrEmpty()) {
            Spacer(Modifier.height(toolbarSize.textSpacer))

            Text(
                text = subtitle,
                style = SquircleTheme.typography.text12Regular,
                color = SquircleTheme.colors.colorTextAndIconSecondary,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}