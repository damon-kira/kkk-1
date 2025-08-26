package com.common.kira.ui.preference

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.extensions.mergeSemantics
import com.common.kira.ui.slider.Slider

@Composable
fun SliderPreference(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minValue: Float = 0f,
    maxValue: Float = 1f,
    currentValue: Float = 0f,
    stepCount: Int = 1,
    onValueChanged: (Float) -> Unit = {},
) {
    Preference(
        title = title,
        subtitle = subtitle,
        enabled = enabled,
        bottomContent = {
            Spacer(Modifier.height(8.dp))
            Slider(
                currentValue = currentValue,
                onValueChanged = onValueChanged,
                enabled = enabled,
                minValue = minValue,
                maxValue = maxValue,
                stepCount = stepCount,
            )
        },
        modifier = modifier.mergeSemantics(),
    )
}

@PreviewLightDark
@Composable
private fun SliderPreferenceEnabledPreview() {
    PreviewBackground {
        SliderPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            enabled = true,
            minValue = 1f,
            maxValue = 8f,
            currentValue = 4f,
            stepCount = 2,
        )
    }
}

@PreviewLightDark
@Composable
private fun SliderPreferenceDisabledPreview() {
    PreviewBackground {
        SliderPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            enabled = false,
            minValue = 1f,
            maxValue = 8f,
            currentValue = 4f,
            stepCount = 2,
        )
    }
}