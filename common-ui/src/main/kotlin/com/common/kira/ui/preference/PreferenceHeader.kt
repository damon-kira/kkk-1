package com.common.kira.ui.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun PreferenceHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .debounceClickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = SquircleTheme.colors.colorTextAndIconPrimary,
            style = SquircleTheme.typography.text16Regular,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            color = SquircleTheme.colors.colorTextAndIconSecondary,
            style = SquircleTheme.typography.text14Regular,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreferenceHeaderPreview() {
    PreviewBackground {
        PreferenceHeader(
            title = "Application",
            subtitle = "Configure global application settings",
        )
    }
}