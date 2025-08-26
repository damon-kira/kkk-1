package com.common.kira.ui.preference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.extensions.clearSemantics

@Composable
fun PreferenceGroup(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 24.dp)
            .clearSemantics()
    ) {
        Text(
            text = title,
            color = SquircleTheme.colors.colorPrimary,
            style = SquircleTheme.typography.text14Medium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun PreferenceGroupPreview() {
    PreviewBackground {
        PreferenceGroup("Group")
    }
}