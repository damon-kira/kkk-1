package com.common.kira.ui.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun Preference(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    leadingContent: @Composable (RowScope.() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    bottomContent: @Composable (ColumnScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .fillMaxWidth()
            .debounceClickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(16.dp)
    ) {
        if (leadingContent != null) {
            leadingContent()
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = SquircleTheme.typography.text16Regular,
                color = if (enabled) {
                    SquircleTheme.colors.colorTextAndIconPrimary
                } else {
                    SquircleTheme.colors.colorTextAndIconDisabled
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            if (subtitle != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = SquircleTheme.typography.text14Regular,
                    color = if (enabled) {
                        SquircleTheme.colors.colorTextAndIconSecondary
                    } else {
                        SquircleTheme.colors.colorTextAndIconDisabled
                    },
                )
            }
            if (bottomContent != null) {
                bottomContent()
            }
        }
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(16.dp))
            trailingContent()
        }
    }
}

@PreviewLightDark
@Composable
private fun PreferenceEnabledPreview() {
    PreviewBackground {
        Preference(
            title = "Squircle CE",
            subtitle = "About application",
            enabled = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreferenceDisabledPreview() {
    PreviewBackground {
        Preference(
            title = "Squircle CE",
            subtitle = "About application",
            enabled = false,
        )
    }
}