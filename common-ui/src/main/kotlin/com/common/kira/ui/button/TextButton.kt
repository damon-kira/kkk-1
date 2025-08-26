package com.common.kira.ui.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    debounce: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    textButtonStyle: TextButtonStyle = TextButtonStyleDefaults.Primary,
    textButtonSize: TextButtonSize = TextButtonSizeDefaults.S,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(
                minWidth = textButtonSize.minWidth,
                minHeight = textButtonSize.minHeight,
            )
            .clip(RoundedCornerShape(textButtonSize.cornerRadius))
            .debounceClickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                debounce = debounce,
                onClick = onClick,
                role = Role.Button,
            )
            .padding(textButtonSize.padding)
    ) {
        Text(
            text = text.uppercase(),
            color = if (enabled) {
                textButtonStyle.enabledTextColor
            } else {
                textButtonStyle.disabledTextColor
            },
            style = textButtonStyle.textStyle,
        )
    }
}

@PreviewLightDark
@Composable
private fun TextButtonEnabledPreview() {
    PreviewBackground {
        TextButton(
            text = "Text Button",
            onClick = {},
            enabled = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun TextButtonDisabledPreview() {
    PreviewBackground {
        TextButton(
            text = "Text Button",
            onClick = {},
            enabled = false,
        )
    }
}