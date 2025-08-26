package com.common.kira.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun ActionLayout(
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    title: String? = null,
    subtitle: String? = null,
    onClick: () -> Unit = {},
    iconColor: Color = SquircleTheme.colors.colorTextAndIconSecondary,
    titleColor: Color = SquircleTheme.colors.colorTextAndIconPrimary,
    subtitleColor: Color = SquircleTheme.colors.colorTextAndIconSecondary,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .debounceClickable(onClick = onClick)
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp,
            )
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = iconColor,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column {
            if (title != null) {
                Text(
                    text = title,
                    style = SquircleTheme.typography.text16Regular,
                    color = titleColor,
                )
            }
            if (title != null && subtitle != null) {
                Spacer(Modifier.height(2.dp))
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = SquircleTheme.typography.text14Regular,
                    color = subtitleColor,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ActionLayoutPreview() {
    PreviewBackground {
        ActionLayout(
            iconRes = R.drawable.ic_autorenew,
            title = "Fetch",
            subtitle = "Fetch content from remote repository",
        )
    }
}