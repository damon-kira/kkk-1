package com.common.kira.ui.popupmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.checkbox.CheckBox

@Composable
@NonRestartableComposable
fun PopupMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    verticalOffset: Dp = 0.dp,
    horizontalOffset: Dp = 0.dp,
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        content = content,
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(horizontalOffset, verticalOffset),
        properties = properties,
        modifier = modifier.background(SquircleTheme.colors.colorBackgroundTertiary),
    )
}

@Composable
fun PopupMenuItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconResId: Int? = null,
    submenu: Boolean = false,
    trailing: @Composable (() -> Unit)? = null,
) {
    val leadingIcon: (@Composable () -> Unit)? = iconResId?.let { resId ->
        {
            Icon(
                painter = painterResource(resId),
                contentDescription = null,
                tint = if (enabled) {
                    SquircleTheme.colors.colorTextAndIconSecondary
                } else {
                    SquircleTheme.colors.colorTextAndIconDisabled
                },
            )
        }
    }
    val trailingIcon: (@Composable () -> Unit)? = when {
        submenu -> {
            {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_right),
                    contentDescription = null,
                    tint = if (enabled) {
                        SquircleTheme.colors.colorTextAndIconSecondary
                    } else {
                        SquircleTheme.colors.colorTextAndIconDisabled
                    },
                )
            }
        }
        trailing != null -> trailing
        else -> null
    }
    DropdownMenuItem(
        text = {
            Text(
                text = title,
                color = if (enabled) {
                    SquircleTheme.colors.colorTextAndIconPrimary
                } else {
                    SquircleTheme.colors.colorTextAndIconDisabled
                },
                style = SquircleTheme.typography.text16Regular,
            )
        },
        onClick = onClick,
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier
            .defaultMinSize(
                minWidth = 172.dp,
                minHeight = Dp.Unspecified,
            )
    )
}

@PreviewLightDark
@Composable
private fun PopupMenuPreview() {
    PreviewBackground {
        PopupMenuItem(
            title = "Menu Item",
            onClick = {},
            iconResId = R.drawable.ic_pencil,
            trailing = { CheckBox(checked = true) }
        )
    }
}