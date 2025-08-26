package com.common.kira.ui.emptyview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.button.OutlinedButton
import com.common.kira.ui.extensions.mergeSemantics

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    iconResId: Int? = null,
    title: String? = null,
    subtitle: String? = null,
    action: String? = null,
    onClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .mergeSemantics()
            .widthIn(max = 320.dp)
            .padding(24.dp),
    ) {
        if (iconResId != null) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = SquircleTheme.colors.colorTextAndIconSecondary,
                modifier = Modifier.size(84.dp)
            )
        }

        if (title != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                color = SquircleTheme.colors.colorTextAndIconPrimary,
                style = SquircleTheme.typography.header20Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        }

        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = SquircleTheme.colors.colorTextAndIconSecondary,
                style = SquircleTheme.typography.text16Regular,
                textAlign = TextAlign.Center,
            )
        }

        if (action != null) {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                text = action,
                onClick = onClick,
                modifier = Modifier.widthIn(min = 100.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun EmptyViewPreview() {
    PreviewBackground {
        EmptyView(
            iconResId = R.drawable.ic_file_find,
            title = "An error occurred",
            subtitle = "Please try again later",
            action = "Try again",
        )
    }
}