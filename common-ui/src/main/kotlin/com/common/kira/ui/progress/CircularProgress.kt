package com.common.kira.ui.progress

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground

@Composable
@NonRestartableComposable
fun CircularProgress(
    circularProgressStyle: CircularProgressStyle = CircularProgressStyleDefaults.Primary,
    circularProgressSize: CircularProgressSize = CircularProgressSizeDefaults.M,
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        color = circularProgressStyle.color,
        strokeWidth = circularProgressSize.strokeWidth,
        modifier = modifier.size(circularProgressSize.circleSize),
    )
}

@PreviewLightDark
@Composable
private fun CircularProgressPreview() {
    PreviewBackground {
        CircularProgress()
    }
}