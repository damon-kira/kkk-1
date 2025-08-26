package com.common.kira.ui.button

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    startIconResId: Int? = null,
    endIconResId: Int? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    outlinedButtonStyle: OutlinedButtonStyle = OutlinedButtonStyleDefaults.Primary,
    outlinedButtonSize: OutlinedButtonSize = OutlinedButtonSizeDefaults.S,
) {
    val buttonShape = RoundedCornerShape(outlinedButtonSize.cornerRadius)
    val buttonColor = if (enabled) {
        outlinedButtonStyle.enabledTextColor
    } else {
        outlinedButtonStyle.disabledTextColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .defaultMinSize(
                minWidth = outlinedButtonSize.minWidth,
                minHeight = outlinedButtonSize.minHeight,
            )
            .clip(buttonShape)
            .debounceClickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
            )
            .border(
                width = outlinedButtonSize.borderSize,
                color = outlinedButtonStyle.borderColor,
                shape = buttonShape,
            )
            .padding(outlinedButtonSize.innerPadding)
    ) {
        if (startIconResId != null) {
            Icon(
                painter = painterResource(startIconResId),
                contentDescription = null,
                tint = buttonColor,
                modifier = Modifier.size(outlinedButtonSize.iconSize),
            )
        }
        Text(
            text = text.uppercase(),
            color = buttonColor,
            style = outlinedButtonStyle.textStyle,
            modifier = Modifier.padding(outlinedButtonSize.textPadding)
        )
        if (endIconResId != null) {
            Icon(
                painter = painterResource(endIconResId),
                contentDescription = null,
                tint = buttonColor,
                modifier = Modifier.size(outlinedButtonSize.iconSize),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun OutlinedButtonEnabledPreview() {
    PreviewBackground {
        OutlinedButton(
            text = "Outlined Button",
            startIconResId = R.drawable.ic_plus,
            onClick = {},
            enabled = true,
        )
    }
}

@PreviewLightDark
@Composable
private fun OutlinedButtonDisabledPreview() {
    PreviewBackground {
        OutlinedButton(
            text = "Outlined Button",
            startIconResId = R.drawable.ic_plus,
            onClick = {},
            enabled = false,
        )
    }
}