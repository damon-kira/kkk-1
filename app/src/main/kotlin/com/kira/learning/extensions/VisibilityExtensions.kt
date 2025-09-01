package com.kira.learning.extensions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.semantics.hideFromAccessibility

/**
 * Compose 下模拟 XML 的 View.VISIBLE / INVISIBLE / GONE 行为的扩展与辅助。
 *
 * 用法速览：
 * 1) GONE（不占位）：
 *    Gone(visible) { Icon(... ) }
 *    // 或 AnimatedGone(visible) { ... } 有淡入淡出
 *
 * 2) INVISIBLE（占位但不可见 & 不可访问）：
 *    Icon(..., modifier = Modifier.invisible(!visible))
 *
 * 3) GONE 修饰符（不建议长期保留节点）：
 *    Modifier.gone(shouldHide) // size(0.dp) + alpha(0f)
 */

/** 不占位呈现：visible=false 时根本不 compose 子树（等价 XML GONE） */
@Composable
fun Gone(visible: Boolean, content: @Composable () -> Unit) {
    if (visible) content()
}

/** 带淡入/淡出动画的不占位呈现 */
@Composable
fun AnimatedGone(visible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
        content()
    }
}

/** 占位但不可见（视觉/可访问性隐藏），等价 XML INVISIBLE */
fun Modifier.invisible(hidden: Boolean): Modifier =
    if (!hidden) this else this
        .alpha(0f)
        .semantics { hideFromAccessibility() }

/** 伪 GONE：仍然 compose 但通过 size(0) 隐藏布局与绘制。优先使用 Gone 以节省开销。 */
fun Modifier.gone(gone: Boolean): Modifier =
    if (!gone) this else this
        .size(0.dp)
        .alpha(0f)
        .semantics { hideFromAccessibility() }

/**
 * 根据是否需要保持占位决定行为：
 * keepSpace=true  -> INVISIBLE
 * keepSpace=false -> GONE
 */
@Composable
fun Visibility(visible: Boolean, keepSpace: Boolean = false, content: @Composable () -> Unit) {
    if (visible) {
        content()
    } else if (keepSpace) {
        // 占位不可见：利用 gone modifier 不绘制（或可换成透明 Box 包裹）
        Gone(true) { /* no-op: 可在需要时改为 Skeleton */ }
    }
}

/** 简易条件渲染（语义糖） */
@Composable
inline fun If(condition: Boolean, content: @Composable () -> Unit) {
    if (condition) content()
}

