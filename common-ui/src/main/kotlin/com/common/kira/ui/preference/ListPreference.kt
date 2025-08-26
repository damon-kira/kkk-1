package com.common.kira.ui.preference

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.dialog.AlertDialog
import com.common.kira.ui.modifier.debounceClickable
import com.common.kira.ui.radio.Radio
import com.common.kira.ui.radio.RadioStyleDefaults

@Composable
fun ListPreference(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    entries: Array<String> = emptyArray(),
    entryValues: Array<String> = emptyArray(),
    entryNameAsSubtitle: Boolean = false,
    selectedValue: String = "",
    onValueSelected: (String) -> Unit = {},
    dialogShown: Boolean = false,
    dialogTitle: String? = null,
) {
    var showDialog by rememberSaveable { mutableStateOf(dialogShown) }
    val displaySubtitle = remember(entries, entryValues, entryNameAsSubtitle) {
        if (entryNameAsSubtitle) {
            val entryIndex = entryValues.indexOf(selectedValue)
            if (entryIndex > -1) {
                entries[entryIndex]
            } else {
                subtitle.toString()
            }
        } else {
            subtitle
        }
    }
    Preference(
        title = title,
        subtitle = displaySubtitle,
        enabled = enabled,
        onClick = { showDialog = true },
        modifier = modifier,
    )
    if (showDialog) {
        AlertDialog(
            title = dialogTitle ?: title,
            verticalScroll = false,
            horizontalPadding = false,
            content = {
                val selectedIndex = entryValues.indexOf(selectedValue)
                val lazyListState = rememberLazyListState(
                    initialFirstVisibleItemIndex = selectedIndex
                )
                LazyColumn(state = lazyListState) {
                    itemsIndexed(entryValues) { index, value ->
                        ListSelection(
                            title = entries[index],
                            selected = index == selectedIndex,
                            onClick = {
                                showDialog = false
                                onValueSelected(value)
                            },
                        )
                    }
                }
            },
            dismissButton = stringResource(android.R.string.cancel),
            onDismissClicked = { showDialog = false },
            onDismiss = { showDialog = false },
        )
    }
}

@Composable
fun ListSelection(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .debounceClickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick,
            )
            .padding(horizontal = 24.dp)
    ) {
        Radio(
            title = title,
            checked = selected,
            onClick = onClick,
            radioStyle = RadioStyleDefaults.Primary.copy(
                textStyle = SquircleTheme.typography.text18Regular
            ),
            interactionSource = interactionSource,
            indication = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun ListPreferencePreview() {
    PreviewBackground {
        ListPreference(
            title = "Application Theme",
            subtitle = "Configure the application theme",
            entries = arrayOf("Light", "Dark", "System default"),
            entryValues = arrayOf("light", "dark", "system"),
            selectedValue = "light",
            dialogShown = true,
        )
    }
}