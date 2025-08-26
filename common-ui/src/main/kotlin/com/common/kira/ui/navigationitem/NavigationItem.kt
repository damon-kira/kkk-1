package com.common.kira.ui.navigationitem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.modifier.debounceClickable
import com.common.kira.ui.R as UiR

@Composable
fun NavigationItem(
    iconResId: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    selected: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    navigationItemStyle: NavigationItemStyle = NavigationItemStyleDefaults.Default,
    navigationItemSize: NavigationItemSize = NavigationItemSizeDefaults.Default,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .defaultMinSize(
                minWidth = navigationItemSize.itemSize.width,
                minHeight = navigationItemSize.itemSize.height,
            )
            .debounceClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .semantics {
                this.selected = selected
            }
    ) {
        val iconColor by animateColorAsState(
            targetValue = when {
                !enabled -> navigationItemStyle.disabledIconColor
                selected -> navigationItemStyle.iconColorSelected
                else -> navigationItemStyle.iconColorUnselected
            }
        )
        val textColor by animateColorAsState(
            targetValue = when {
                !enabled -> navigationItemStyle.disabledTextColor
                selected -> navigationItemStyle.textColorSelected
                else -> navigationItemStyle.textColorUnselected
            }
        )
        val indicatorProgress by animateFloatAsState(
            targetValue = if (selected) 1f else 0f
        )
        val indicatorRipple = @Composable {
            Box(
                Modifier
                    .size(navigationItemSize.indicatorSize)
                    .clip(CircleShape)
                    .indication(interactionSource, ripple())
            )
        }
        val indicator = @Composable {
            Box(
                modifier = Modifier
                    .size(navigationItemSize.indicatorSize)
                    .graphicsLayer {
                        alpha = indicatorProgress
                        scaleX = indicatorProgress
                    }
                    .background(
                        color = SquircleTheme.colors.colorBackgroundTertiary,
                        shape = CircleShape
                    )
            )
        }

        Box(contentAlignment = Alignment.Center) {
            indicatorRipple()
            indicator()

            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = iconColor
            )
        }

        Spacer(Modifier.height(navigationItemSize.labelSpacer))

        Text(
            text = label,
            style = navigationItemSize.labelTextStyle,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun NavigationItemPreview() {
    PreviewBackground {
        var selectedIndex by remember { mutableIntStateOf(0) }
        Column {
            NavigationItem(
                iconResId = UiR.drawable.ic_folder,
                label = "Local",
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0 },
            )
            NavigationItem(
                iconResId = UiR.drawable.ic_folder_pound,
                label = "Root",
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1 },
            )
        }
    }
}