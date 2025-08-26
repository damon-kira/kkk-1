package com.common.kira.ui.textfield.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.common.kira.ui.extensions.clearSemantics

@Composable
@NonRestartableComposable
internal fun DecorationBox(
    inputText: CharSequence,
    inputTextField: @Composable () -> Unit,
    placeholder: String,
    placeholderTextStyle: TextStyle,
    placeholderTextColor: Color,
    modifier: Modifier = Modifier,
) {
    val isPlaceholderVisible = inputText.isEmpty() && placeholder.isNotEmpty()

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
    ) {
        if (isPlaceholderVisible) {
            TextFieldPlaceholder(
                text = placeholder,
                textStyle = placeholderTextStyle,
                textColor = placeholderTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clearSemantics(),
            )
        }
        inputTextField()
    }
}