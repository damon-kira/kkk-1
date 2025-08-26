package com.common.kira.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.SquircleTheme

@Composable
fun ThreeSlotLayout(
    modifier: Modifier = Modifier,
    middleContent: @Composable (BoxScope.() -> Unit)? = null,
    startContent: @Composable (BoxScope.() -> Unit)? = null,
    endContent: @Composable (BoxScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (startContent != null) {
            Box(
                contentAlignment = Alignment.Center,
                content = startContent,
            )
        }
        if (middleContent != null) {
            Box(
                contentAlignment = Alignment.Center,
                content = middleContent,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
        if (endContent != null) {
            Box(
                contentAlignment = Alignment.Center,
                content = endContent,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ThreeSlotLayoutPreview() {
    PreviewBackground {
        ThreeSlotLayout(
            startContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                )
            },
            middleContent = {
                Text(
                    text = "Example",
                    color = SquircleTheme.colors.colorTextAndIconPrimary,
                    style = SquircleTheme.typography.text16Regular,
                )
            },
            endContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                )
            }
        )
    }
}