package com.common.kira.ui.preference

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.dialog.AlertDialog
import com.common.kira.ui.textfield.TextField
import com.common.kira.ui.textfield.TextFieldStyleDefaults

@Composable
fun TextFieldPreference(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    confirmButton: String? = null,
    dismissButton: String? = null,
    labelText: String? = null,
    helpText: String? = null,
    inputTextStyle: TextStyle = LocalTextStyle.current,
    inputValue: String = "",
    onConfirmClicked: (String) -> Unit = {},
    onDismissClicked: () -> Unit = {},
    dialogShown: Boolean = false,
    dialogTitle: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var showDialog by rememberSaveable { mutableStateOf(dialogShown) }
    Preference(
        title = title,
        subtitle = subtitle,
        enabled = enabled,
        onClick = { showDialog = true },
        modifier = modifier,
    )
    if (showDialog) {
        val text = rememberSaveable { mutableStateOf(inputValue) }
        AlertDialog(
            title = dialogTitle ?: title,
            content = {
                TextField(
                    inputText = text.value,
                    onInputChanged = { text.value = it },
                    labelText = labelText,
                    helpText = helpText,
                    textFieldStyle = TextFieldStyleDefaults.Default.copy(
                        textStyle = inputTextStyle,
                    ),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                )
            },
            confirmButton = confirmButton,
            onConfirmClicked = {
                showDialog = false
                onConfirmClicked(text.value)
            },
            dismissButton = dismissButton,
            onDismissClicked = {
                showDialog = false
                onDismissClicked()
            },
            onDismiss = { showDialog = false },
        )
    }
}

@PreviewLightDark
@Composable
private fun TextFieldPreferencePreview() {
    PreviewBackground {
        TextFieldPreference(
            title = "Title",
            subtitle = "Subtitle",
            inputValue = "Hello World!",
            labelText = "Label Text",
            helpText = "Help Text",
            confirmButton = "Save",
            dismissButton = "Cancel",
            dialogShown = true,
        )
    }
}