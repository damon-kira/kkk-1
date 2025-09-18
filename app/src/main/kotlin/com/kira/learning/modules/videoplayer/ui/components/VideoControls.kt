package com.kira.learning.modules.videoplayer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kira.learning.models.VideoNote
import com.kira.learning.models.formatTime
import kotlinx.coroutines.delay

/**
 * 视频控制器组件
 * 包含播放控制、进度条、音量控制等功能
 * 支持自动隐藏和点击显示
 */
@Composable
fun VideoControls(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    volume: Float,
    playbackSpeed: Float,
    isFullscreen: Boolean,
    notes: List<VideoNote>,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onFullscreenToggle: () -> Unit,
    onAddNote: (Long) -> Unit,
    onNoteClick: (VideoNote) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }
    var showCenterPlayButton by remember { mutableStateOf(!isPlaying) }

    // 自动隐藏控制器逻辑
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(3000) // 3秒后自动隐藏
            showControls = false
        }
    }

    // 监听播放状态变化，暂停时显示中央播放按钮
    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            showCenterPlayButton = true
        } else {
            // 播放时短暂显示然后隐藏
            showCenterPlayButton = true
            delay(1000)
            showCenterPlayButton = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // 点击视频区域切换控制器显示状态
                showControls = !showControls
            }
    ) {
        // 中央播放/暂停按钮 - 只在暂停时或短暂时间内显示
        AnimatedVisibility(
            visible = showCenterPlayButton,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            IconButton(
                onClick = {
                    onPlayPause()
                    if (isPlaying) {
                        showCenterPlayButton = false
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "暂停" else "播放",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // 完整的控制器界面 - 可以隐藏
        AnimatedVisibility(
            visible = showControls,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.3f)
                    )
                    .padding(16.dp)
            ) {
                // 顶部控制栏
                TopControlBar(
                    isFullscreen = isFullscreen,
                    onBack = onBack,
                    onFullscreenToggle = onFullscreenToggle
                )

                Spacer(modifier = Modifier.weight(1f))

                // 底部控制栏
                BottomControlBar(
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    duration = duration,
                    volume = volume,
                    playbackSpeed = playbackSpeed,
                    notes = notes,
                    onPlayPause = onPlayPause,
                    onSeek = onSeek,
                    onVolumeChange = onVolumeChange,
                    onSpeedChange = onSpeedChange,
                    onAddNote = onAddNote,
                    onNoteClick = onNoteClick
                )
            }
        }
    }
}

@Composable
private fun TopControlBar(
    isFullscreen: Boolean,
    onBack: () -> Unit,
    onFullscreenToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp), // 添加顶部padding，让按钮向下移动
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(48.dp) // 增大按钮点击区域
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回",
                tint = Color.White,
                modifier = Modifier.size(24.dp) // 图标大小保持合适
            )
        }

        IconButton(
            onClick = onFullscreenToggle,
            modifier = Modifier
                .size(48.dp) // 增大按钮点击区域
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isFullscreen) "退出全屏" else "全屏",
                tint = Color.White,
                modifier = Modifier.size(24.dp) // 图标大小保持合适
            )
        }
    }
}

@Composable
private fun BottomControlBar(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    volume: Float,
    playbackSpeed: Float,
    notes: List<VideoNote>,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onAddNote: (Long) -> Unit,
    onNoteClick: (VideoNote) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.7f),
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        // 进度条和笔记标记
        VideoProgressBar(
            currentPosition = currentPosition,
            duration = duration,
            notes = notes,
            onSeek = onSeek,
            onNoteClick = onNoteClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 控制按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 播放/暂停按钮
            IconButton(onClick = onPlayPause) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "暂停" else "播放",
                    tint = Color.White
                )
            }

            // 时间显示
            Text(
                text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )

            // 倍速按钮
            SpeedButton(
                currentSpeed = playbackSpeed,
                onSpeedChange = onSpeedChange
            )

            // 音量控制
            VolumeControl(
                volume = volume,
                onVolumeChange = onVolumeChange
            )

            // 添加笔记按钮
            IconButton(
                onClick = { onAddNote(currentPosition) }
            ) {
                Icon(
                    imageVector = Icons.Default.Note,
                    contentDescription = "添加笔记",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun SpeedButton(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit
) {
    var showSpeedMenu by remember { mutableStateOf(false) }
    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    Box {
        TextButton(
            onClick = { showSpeedMenu = true }
        ) {
            Text(
                text = "${currentSpeed}x",
                color = Color.White
            )
        }

        DropdownMenu(
            expanded = showSpeedMenu,
            onDismissRequest = { showSpeedMenu = false }
        ) {
            speeds.forEach { speed ->
                DropdownMenuItem(
                    text = { Text("${speed}x") },
                    onClick = {
                        onSpeedChange(speed)
                        showSpeedMenu = false
                    }
                )
            }
        }
    }
}

@Composable
private fun VolumeControl(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    var showVolumeSlider by remember { mutableStateOf(false) }

    Column {
        IconButton(
            onClick = { showVolumeSlider = !showVolumeSlider }
        ) {
            Icon(
                imageVector = when {
                    volume == 0f -> Icons.Default.VolumeOff
                    volume < 0.5f -> Icons.Default.VolumeDown
                    else -> Icons.Default.VolumeUp
                },
                contentDescription = "音量",
                tint = Color.White
            )
        }

        if (showVolumeSlider) {
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                modifier = Modifier.width(100.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White
                )
            )
        }
    }
}
