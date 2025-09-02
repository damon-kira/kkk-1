package com.kira.learning.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest

data class ImageTheme(
    val placeholderColor: Color,
    val errorColor: Color,
    val iconTint: Color,
    val cornerRadius: Dp = 8.dp,
    val crossfade: Boolean = true,
)

val LocalImageTheme = staticCompositionLocalOf<ImageTheme> {
    ImageTheme(
        placeholderColor = Color.LightGray.copy(alpha = 0.3f),
        errorColor = Color.Red.copy(alpha = 0.4f),
        iconTint = Color.DarkGray,
    )
}

@Composable
fun ProvideImageTheme(theme: ImageTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalImageTheme provides theme) { content() }
}

/** 高级图片加载封装
 * 功能:
 * 1. 统一占位/错误UI主题化
 * 2. 支持关闭/开启内存与磁盘缓存策略
 * 3. 支持圆形/自定义圆角/自定义形状
 * 4. Crossfade 过渡
 * 5. 回调: onSuccess / onError
 */
@Composable
fun AppImage(
    url: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(LocalImageTheme.current.cornerRadius),
    cornerRadius: Dp? = null, // 若传入则覆盖 shape 为圆角
    circle: Boolean = false,
    memoryCache: Boolean = true,
    diskCache: Boolean = true,
    crossfade: Boolean = LocalImageTheme.current.crossfade,
    backgroundColor: Color = LocalImageTheme.current.placeholderColor,
    iconTint: Color = LocalImageTheme.current.iconTint,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
) {
    val finalShape = when {
        circle -> CircleShape
        cornerRadius != null -> RoundedCornerShape(cornerRadius)
        else -> shape
    }

    val request = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .memoryCachePolicy(if (memoryCache) CachePolicy.ENABLED else CachePolicy.DISABLED)
        .diskCachePolicy(if (diskCache) CachePolicy.ENABLED else CachePolicy.DISABLED)
        .apply { if (crossfade) crossfade(true) }
        .build()

    Surface(shape = finalShape, tonalElevation = 0.dp, modifier = modifier, color = Color.Transparent) {
        SubcomposeAsyncImage(
            model = request,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
        ) {
            val state = painter.state
            when {
                state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Empty -> PlaceholderBox(backgroundColor, iconTint, Icons.Default.Image)
                state is AsyncImagePainter.State.Error -> {
                    onError?.invoke(state.result.throwable)
                    PlaceholderBox(backgroundColor, LocalImageTheme.current.errorColor, Icons.Default.BrokenImage, isError = true)
                }
                state is AsyncImagePainter.State.Success -> {
                    onSuccess?.invoke()
                    SubcomposeAsyncImageContent()
                }
                else -> SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
private fun PlaceholderBox(bg: Color, tint: Color, icon: ImageVector, isError: Boolean = false) {
    Box(
        modifier = Modifier
            .background(bg)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = if (isError) MaterialTheme.colorScheme.error else tint)
    }
}

/** 头像快捷组件 */
@Composable
fun AppAvatar(
    url: Any?,
    size: Dp,
    modifier: Modifier = Modifier,
    memoryCache: Boolean = true,
    diskCache: Boolean = true,
    onSuccess: (() -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
) {
    AppImage(
        url = url,
        modifier = modifier.size(size),
        circle = true,
        memoryCache = memoryCache,
        diskCache = diskCache,
        onSuccess = onSuccess,
        onError = onError,
        contentDescription = "avatar"
    )
}
