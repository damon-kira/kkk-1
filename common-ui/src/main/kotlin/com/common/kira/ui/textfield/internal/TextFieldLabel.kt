package com.common.kira.ui.textfield.internal

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
@NonRestartableComposable
internal fun TextFieldLabel(
    text: String?,
    textStyle: TextStyle,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.orEmpty(),
        style = textStyle,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}