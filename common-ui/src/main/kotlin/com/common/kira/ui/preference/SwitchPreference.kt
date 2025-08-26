package com.common.kira.ui.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.extensions.mergeSemantics
import com.common.kira.ui.switcher.Switcher

@Composable
fun SwitchPreference(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Preference(
        title = title,
        subtitle = subtitle,
        enabled = enabled,
        onClick = { onCheckedChange(!checked) },
        trailingContent = {
            Switcher(
                onClick = { onCheckedChange(!checked) },
                checked = checked,
                enabled = enabled,
            )
        },
        modifier = modifier.mergeSemantics(),
    )
}

@PreviewLightDark
@Composable
private fun EnabledSwitchPreferenceCheckedPreview() {
    PreviewBackground {
        SwitchPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            checked = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun EnabledSwitchPreferenceUncheckedPreview() {
    PreviewBackground {
        SwitchPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            checked = false,
        )
    }
}

@PreviewLightDark
@Composable
private fun DisabledSwitchPreferenceCheckedPreview() {
    PreviewBackground {
        SwitchPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            enabled = false,
            checked = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun DisabledSwitchPreferenceUncheckedPreview() {
    PreviewBackground {
        SwitchPreference(
            title = "Preference Title",
            subtitle = "Preference Subtitle",
            enabled = false,
            checked = false,
        )
    }
}