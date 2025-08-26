package com.common.kira.ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.common.kira.ui.SquircleTheme

@Composable
fun TabIndicator(
    active: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = active,
        enter = slideInVertically(
            animationSpec = tween(),
            initialOffsetY = { it },
        ),
        exit = slideOutVertically(
            animationSpec = tween(),
            targetOffsetY = { it },
        ),
        modifier = modifier.clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    color = SquircleTheme.colors.colorPrimary,
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                )
        )
    }
}