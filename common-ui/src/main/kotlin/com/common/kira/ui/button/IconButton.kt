package com.common.kira.ui.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.modifier.debounceClickable

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    iconResId: Int? = null,
    onClick: () -> Unit = {},
    contentDescription: String? = null,
    enabled: Boolean = true,
    debounce: Boolean = true,
    anchor: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    iconButtonStyle: IconButtonStyle = IconButtonStyleDefaults.Primary,
    iconButtonSize: IconButtonSize = IconButtonSizeDefaults.M,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(iconButtonSize.iconSize)
            .debounceClickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    radius = iconButtonSize.rippleSize,
                ),
                onClick = onClick,
                enabled = enabled,
                debounce = debounce,
                role = Role.Button,
            )
    ) {
        if (iconResId != null) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = contentDescription,
                tint = if (enabled) {
                    iconButtonStyle.iconColor
                } else {
                    iconButtonStyle.disabledIconColor
                },
            )
        }
        anchor?.invoke()
    }
}

@PreviewLightDark
@Composable
private fun IconButtonPreview() {
    PreviewBackground {
        Row {
            IconButton(
                iconResId = R.drawable.ic_pencil,
                iconButtonSize = IconButtonSizeDefaults.S,
            )
            IconButton(
                iconResId = R.drawable.ic_pencil,
                iconButtonSize = IconButtonSizeDefaults.M,
            )
            IconButton(
                iconResId = R.drawable.ic_pencil,
                iconButtonSize = IconButtonSizeDefaults.L,
            )
        }
    }
}