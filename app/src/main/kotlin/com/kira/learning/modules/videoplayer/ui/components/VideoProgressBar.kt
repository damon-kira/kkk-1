package com.kira.learning.modules.videoplayer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kira.learning.models.VideoNote
import com.kira.learning.models.toColor

/**
 * 带笔记标记的视频进度条组件
 */
@Composable
fun VideoProgressBar(
    currentPosition: Long,
    duration: Long,
    notes: List<VideoNote>,
    onSeek: (Long) -> Unit,
    onNoteClick: (VideoNote) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var canvasWidth by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (duration > 0) {
                            val clickPosition = (offset.x / canvasWidth * duration).toLong()
                            onSeek(clickPosition.coerceIn(0, duration))
                        }
                    }
                }
        ) {
            canvasWidth = size.width
            drawProgressBar(
                currentPosition = currentPosition,
                duration = duration
            )
        }

        // 笔记标记点
        notes.forEach { note ->
            if (duration > 0) {
                val position = (note.position.toFloat() / duration * canvasWidth)
                Box(
                    modifier = Modifier
                        .offset(x = with(density) { position.toDp() } - 6.dp)
                        .size(12.dp)
                        .background(note.color.toColor(), CircleShape)
                        .clickable { onNoteClick(note) }
                )
            }
        }
    }
}

private fun DrawScope.drawProgressBar(
    currentPosition: Long,
    duration: Long
) {
    val trackHeight = 8.dp.toPx()
    val trackY = (size.height - trackHeight) / 2

    // 绘制背景轨道
    drawRect(
        color = Color.Gray.copy(alpha = 0.3f),
        topLeft = Offset(0f, trackY),
        size = androidx.compose.ui.geometry.Size(size.width, trackHeight)
    )

    // 绘制播放进度
    if (duration > 0) {
        val progress = currentPosition.toFloat() / duration
        drawRect(
            color = Color.White,
            topLeft = Offset(0f, trackY),
            size = androidx.compose.ui.geometry.Size(size.width * progress, trackHeight)
        )
    }
}
