package com.common.kira.ui.slider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme

private const val InactiveTrackAlpha = 0.24f
private const val DisabledInactiveTrackAlpha = 0.12f
private const val TickAlpha = 0.54f
private const val DisabledTickAlpha = 0.12f

@Composable
fun Slider(
    currentValue: Float,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    displayCount: Boolean = true,
    enabled: Boolean = true,
    stepCount: Int = 1,
    minValue: Float = 0f,
    maxValue: Float = 1f,
    sliderStyle: SliderStyle = SliderStyleDefaults.Primary,
) {
    var displayValueHolder by rememberSaveable {
        mutableFloatStateOf(currentValue)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .requiredHeight(32.dp)
                .weight(1f)
        ) {
            Slider(
                value = displayValueHolder,
                valueRange = minValue..maxValue,
                steps = stepCount,
                onValueChange = { displayValueHolder = it },
                onValueChangeFinished = { onValueChanged(displayValueHolder) },
                enabled = enabled,
                colors = SliderDefaults.colors(
                    thumbColor = sliderStyle.thumbColor,
                    disabledThumbColor = sliderStyle.disabledThumbColor,
                    activeTrackColor = sliderStyle.trackColor,
                    inactiveTrackColor = sliderStyle.trackColor.copy(alpha = InactiveTrackAlpha),
                    disabledActiveTrackColor = sliderStyle.disabledTrackColor,
                    disabledInactiveTrackColor = sliderStyle.disabledTrackColor.copy(alpha = DisabledInactiveTrackAlpha),
                    activeTickColor = sliderStyle.tickColor,
                    inactiveTickColor = sliderStyle.tickColor.copy(alpha = TickAlpha),
                    disabledActiveTickColor = sliderStyle.disabledTickColor,
                    disabledInactiveTickColor = sliderStyle.disabledTickColor.copy(alpha = DisabledTickAlpha),
                )
            )
        }
        if (displayCount) {
            Text(
                text = displayValueHolder.toInt().toString(),
                style = SquircleTheme.typography.text16Regular,
                color = if (enabled) {
                    SquircleTheme.colors.colorTextAndIconPrimary
                } else {
                    SquircleTheme.colors.colorTextAndIconDisabled
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SliderEnabledPreview() {
    PreviewBackground {
        var currentValue by remember { mutableFloatStateOf(5f) }
        Slider(
            currentValue = currentValue,
            onValueChanged = { currentValue = it },
            enabled = true,
            minValue = 1f,
            maxValue = 10f,
            stepCount = 8,
        )
    }
}

@PreviewLightDark
@Composable
private fun SliderDisabledPreview() {
    PreviewBackground {
        var currentValue by remember { mutableFloatStateOf(5f) }
        Slider(
            currentValue = currentValue,
            onValueChanged = { currentValue = it },
            enabled = false,
            minValue = 1f,
            maxValue = 10f,
            stepCount = 8,
        )
    }
}