package com.common.kira.ui.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = SquircleTheme.colors.colorOutline,
) {
    Canvas(
        modifier
            .fillMaxHeight()
            .width(thickness)
    ) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            start = Offset(thickness.toPx() / 2, 0f),
            end = Offset(thickness.toPx() / 2, size.height),
        )
    }
}

@PreviewLightDark
@Composable
private fun VerticalDividerPreview() {
    PreviewBackground {
        VerticalDivider()
    }
}