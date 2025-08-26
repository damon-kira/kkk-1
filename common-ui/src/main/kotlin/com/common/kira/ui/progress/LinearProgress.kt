package com.common.kira.ui.progress

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme

@Composable
@NonRestartableComposable
fun LinearProgress(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    indeterminate: Boolean = false,
) {
    if (indeterminate) {
        LinearProgressIndicator(
            color = SquircleTheme.colors.colorPrimary,
            modifier = modifier,
        )
    } else {
        LinearProgressIndicator(
            progress = progress,
            color = SquircleTheme.colors.colorPrimary,
            modifier = modifier,
        )
    }
}

@PreviewLightDark
@Composable
private fun LinearProgressPreview() {
    PreviewBackground {
        LinearProgress(progress = 0.7f)
    }
}