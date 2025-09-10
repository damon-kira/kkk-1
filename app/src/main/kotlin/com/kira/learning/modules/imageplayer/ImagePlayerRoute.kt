package com.kira.learning.modules.imageplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import com.kira.learning.utils.AppImage
import kotlin.math.abs

/** 简单图片播放器：支持放大缩小、拖动、上一张 / 下一张 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePlayerRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
) {
    // 示例图片（可替换为项目资源或动态数据）
    val images = remember {
        listOf(
            // 使用网络示例图片，后续可改为本地资源
            "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?w=1200",
            "https://images.unsplash.com/photo-1499084732479-de2c02d45fcc?w=1200",
            "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=1200",
        )
    }
    var index by remember { mutableStateOf(0) }

    // 变换状态
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, 5f)
        if (abs(newScale - scale) > 0.0001f) {
            // 缩放中心默认是手势中心，Compose 已处理偏移，这里只更新 scale
            scale = newScale
        }
        if (scale > 1f) {
            offsetX += panChange.x
            offsetY += panChange.y
        } else {
            offsetX = 0f; offsetY = 0f
        }
    }

    fun resetZoom() { scale = 1f; offsetX = 0f; offsetY = 0f }

    // 当图片切换时重置状态
    LaunchedEffect(index) { resetZoom() }

    Scaffold(topBar = {
        TopAppBar(title = { Text("图片播放器") }, navigationIcon = {
            IconButton(onClick = openDrawer) { Icon(Icons.Default.Refresh, contentDescription = "菜单") }
        }, actions = {
            TextButton(onClick = { resetZoom() }) { Text("复位") }
        })
    }) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            Text("${index + 1} / ${images.size}", fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clipToBounds(),
                contentAlignment = Alignment.Center
            ) {
                // 图片区域
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(index, scale) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (scale > 1f) resetZoom() else scale = 2.2f
                                }
                            )
                        }
                        .transformable(transformState)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AppImage(
                        url = images[index],
                        contentDescription = "image-${index}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = if (scale > 1f) ContentScale.Fit else ContentScale.Fit,
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { if (index > 0) index-- }, enabled = index > 0) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(4.dp)); Text("上一张")
                }
                Button(onClick = { resetZoom() }, enabled = scale != 1f) { Text("复位") }
                OutlinedButton(onClick = { if (index < images.lastIndex) index++ }, enabled = index < images.lastIndex) {
                    Text("下一张"); Spacer(Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
