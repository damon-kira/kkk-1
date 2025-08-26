package com.common.kira.ui.switcher

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.extensions.clearSemantics
import com.common.kira.ui.extensions.mergeSemantics
import com.common.kira.ui.modifier.debounceToggleable

private const val CheckedTrackAlpha = 0.54f
private const val UncheckedTrackAlpha = 0.38f

@Composable
fun Switcher(
    modifier: Modifier = Modifier,
    title: String? = null,
    onClick: () -> Unit = {},
    checked: Boolean = true,
    enabled: Boolean = true,
    switcherStyle: SwitcherStyle = SwitcherStyleDefaults.Primary,
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .mergeSemantics()
            .debounceToggleable(
                interactionSource = interactionSource,
                indication = null,
                value = checked,
                enabled = enabled,
                onClick = onClick,
            )
    ) {
        Box(Modifier.requiredSize(42.dp)) {
            Switch(
                checked = checked,
                onCheckedChange = { onClick() },
                enabled = enabled,
                interactionSource = interactionSource,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = switcherStyle.checkedColor,
                    checkedTrackColor = switcherStyle.checkedColor.copy(alpha = CheckedTrackAlpha),
                    uncheckedThumbColor = switcherStyle.uncheckedColor,
                    uncheckedTrackColor = switcherStyle.uncheckedColor.copy(alpha = UncheckedTrackAlpha),
                    disabledCheckedThumbColor = switcherStyle.disabledColor,
                    disabledCheckedTrackColor = switcherStyle.disabledColor.copy(alpha = CheckedTrackAlpha),
                    disabledUncheckedThumbColor = switcherStyle.disabledColor,
                    disabledUncheckedTrackColor = switcherStyle.disabledColor.copy(alpha = UncheckedTrackAlpha),
                ),
                modifier = Modifier.clearSemantics()
            )
        }
        if (title != null) {
            Text(
                text = title,
                style = switcherStyle.textStyle,
                color = if (enabled) {
                    switcherStyle.enabledTextColor
                } else {
                    switcherStyle.disabledTextColor
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SwitcherCheckedPreview() {
    PreviewBackground {
        Switcher(
            title = "Switcher",
            checked = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun SwitcherUncheckedPreview() {
    PreviewBackground {
        Switcher(
            title = "Switcher",
            checked = false,
        )
    }
}

@PreviewLightDark
@Composable
private fun SwitcherCheckedDisabledPreview() {
    PreviewBackground {
        Switcher(
            title = "Switcher",
            checked = true,
            enabled = false,
        )
    }
}

@PreviewLightDark
@Composable
private fun SwitcherUncheckedDisabledPreview() {
    PreviewBackground {
        Switcher(
            title = "Switcher",
            checked = false,
            enabled = false,
        )
    }
}