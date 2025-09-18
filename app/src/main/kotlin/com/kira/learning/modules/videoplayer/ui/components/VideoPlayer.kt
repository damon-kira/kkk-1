package com.kira.learning.modules.videoplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.kira.learning.models.Video

/**
 * 视频播放器组件
 * 使用ExoPlayer实现内嵌视频播放
 */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    video: Video?,
    isPlaying: Boolean,
    currentPosition: Long,
    isBuffering: Boolean,
    onPositionChanged: (Long) -> Unit,
    onDurationChanged: (Long) -> Unit,
    onBufferingChanged: (Boolean) -> Unit,
    onError: (String) -> Unit,
    onReady: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 创建ExoPlayer实例
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // 设置播放器监听器
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            onReady()
                            onDurationChanged(this@apply.duration)
                            onBufferingChanged(false) // 修复：播放器准备好时停止缓冲状态
                        }

                        Player.STATE_BUFFERING -> {
                            onBufferingChanged(true)
                        }

                        Player.STATE_ENDED -> {
                            onBufferingChanged(false)
                        }

                        Player.STATE_IDLE -> {
                            onBufferingChanged(false)
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    onError(error.message ?: "播放出错")
                    onBufferingChanged(false) // 修复：出错时也要停止缓冲状态
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    if (!playing && this@apply.playbackState != Player.STATE_BUFFERING) {
                        onBufferingChanged(false) // 修复：非缓冲状态下暂停时停止loading
                    }
                }
            })
        }
    }

    // 监听视频变化，加载新视频
    LaunchedEffect(video) {
        video?.let {
            val mediaItem = MediaItem.fromUri(it.url.toUri())
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    // 监听播放状态变化
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // 监听外部位置变化（用于拖拽进度条）
    LaunchedEffect(currentPosition) {
        if (kotlin.math.abs(exoPlayer.currentPosition - currentPosition) > 1000) {
            exoPlayer.seekTo(currentPosition)
        }
    }

    // 定期更新播放进度
    LaunchedEffect(exoPlayer) {
        while (true) {
            if (exoPlayer.isPlaying) {
                onPositionChanged(exoPlayer.currentPosition)
                onDurationChanged(exoPlayer.duration.takeIf { it > 0 } ?: 0)
            }
            kotlinx.coroutines.delay(1000) // 每秒更新一次
        }
    }

    // 销毁时释放播放器
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    if (video != null) {
        // 使用AndroidView嵌入原生PlayerView
        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false // 使用自定义控制器
                        setBackgroundColor(android.graphics.Color.BLACK)
                        // 设置视频缩放模式，保持宽高比的同时填充可用空间
                        try {
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        } catch (e: Exception) {
                            // 如果设置缩放模式失败，忽略错误继续播放
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )

            // 在视频播放器上方显示缓冲指示器
            if (isBuffering) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    } else {
        // 占位界面
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "请选择视频",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
