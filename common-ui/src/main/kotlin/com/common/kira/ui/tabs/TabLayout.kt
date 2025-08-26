package com.common.kira.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.common.kira.ui.divider.HorizontalDivider

@Composable
fun TabLayout(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    divider: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    Box(modifier) {
        Row(Modifier.zIndex(1f)) {
            if (leadingContent != null) {
                leadingContent()
            }
            LazyRow(
                state = state,
                content = content,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
            )
            if (trailingContent != null) {
                trailingContent()
            }
        }
        if (divider) {
            HorizontalDivider(Modifier.align(Alignment.BottomCenter))
        }
    }
}