

package com.common.kira.ui.button

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.common.kira.ui.PreviewBackground
import com.common.kira.ui.R
import com.common.kira.ui.SquircleTheme

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    iconResId: Int? = null,
    onClick: () -> Unit = {},
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        if (iconResId != null) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = SquircleTheme.colors.colorTextAndIconPrimaryInverse,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun FloatingButtonPreview() {
    PreviewBackground {
        FloatingButton(
            iconResId = R.drawable.ic_pencil,
        )
    }
}