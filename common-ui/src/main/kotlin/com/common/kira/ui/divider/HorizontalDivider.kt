package com.common.kira.ui.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = SquircleTheme.colors.colorOutline,
) {
    Canvas(
        modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            start = Offset(0f, thickness.toPx() / 2),
            end = Offset(size.width, thickness.toPx() / 2),
        )
    }
}

@PreviewLightDark
@Composable
private fun HorizontalDividerPreview() {
    PreviewBackground {
        HorizontalDivider()
    }
}