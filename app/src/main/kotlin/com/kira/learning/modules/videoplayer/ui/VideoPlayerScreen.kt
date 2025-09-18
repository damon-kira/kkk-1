package com.kira.learning.modules.videoplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.models.Video
import com.kira.learning.modules.videoplayer.VideoPlayerEvent
import com.kira.learning.modules.videoplayer.VideoPlayerViewModel
import com.kira.learning.modules.videoplayer.ui.components.VideoPlayer
import com.kira.learning.modules.videoplayer.ui.components.VideoControls
import com.kira.learning.modules.videoplayer.ui.components.NotesPanel
import com.kira.learning.modules.videoplayer.ui.components.AddNoteDialog

/**
 * 视频播放器主界面
 * 重新设计布局，优化视频显示比例和空间利用
 */
@Composable
fun VideoPlayerScreen(
    video: Video,
    onBackClick: () -> Unit = {},
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()

    // 初始化加载视频
    LaunchedEffect(video) {
        viewModel.handleAction(VideoPlayerEvent.LoadVideo(video))
    }

    // 处理UI事件
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            // 处理通用UI事件，如显示错误消息等
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (uiState.isLoading) {
            // 加载状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (uiState.error != null) {
            // 错误状态
            ErrorScreen(
                error = uiState.error!!,
                onRetry = { viewModel.handleAction(VideoPlayerEvent.RetryLoad) },
                onBack = onBackClick
            )
        } else {
            if (uiState.isFullscreen) {
                // 全屏模式：视频占满整个屏幕，隐藏系统UI
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    VideoPlayer(
                        video = uiState.video,
                        isPlaying = uiState.isPlaying,
                        currentPosition = uiState.currentPosition,
                        isBuffering = uiState.isBuffering,
                        onPositionChanged = { position ->
                            viewModel.handleAction(VideoPlayerEvent.OnPositionChanged(position))
                        },
                        onDurationChanged = { duration ->
                            viewModel.handleAction(VideoPlayerEvent.OnDurationChanged(duration))
                        },
                        onBufferingChanged = { isBuffering ->
                            viewModel.handleAction(VideoPlayerEvent.OnBufferingChanged(isBuffering))
                        },
                        onError = { error ->
                            viewModel.handleAction(VideoPlayerEvent.OnError(error))
                        },
                        onReady = {
                            viewModel.handleAction(VideoPlayerEvent.OnVideoReady)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // 全屏模式下的控制器 - 始终显示退出全屏按钮
                    VideoControls(
                        isPlaying = uiState.isPlaying,
                        currentPosition = uiState.currentPosition,
                        duration = uiState.duration,
                        volume = uiState.volume,
                        playbackSpeed = uiState.playbackSpeed,
                        isFullscreen = uiState.isFullscreen,
                        notes = uiState.notes,
                        onPlayPause = {
                            if (uiState.isPlaying) {
                                viewModel.handleAction(VideoPlayerEvent.Pause)
                            } else {
                                viewModel.handleAction(VideoPlayerEvent.Play)
                            }
                        },
                        onSeek = { position ->
                            viewModel.handleAction(VideoPlayerEvent.SeekTo(position))
                        },
                        onVolumeChange = { volume ->
                            viewModel.handleAction(VideoPlayerEvent.SetVolume(volume))
                        },
                        onSpeedChange = { speed ->
                            viewModel.handleAction(VideoPlayerEvent.SetPlaybackSpeed(speed))
                        },
                        onFullscreenToggle = {
                            viewModel.handleAction(VideoPlayerEvent.ToggleFullscreen)
                        },
                        onAddNote = { position ->
                            viewModel.handleAction(VideoPlayerEvent.ShowNoteDialog(position))
                        },
                        onNoteClick = { note ->
                            viewModel.handleAction(VideoPlayerEvent.SelectNote(note))
                        },
                        onBack = onBackClick
                    )
                }
            } else {
                // 常规模式：使用上下分割布局，视频占据更多空间
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 视频播放器区域 - 占据屏幕上半部分的大部分空间
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.65f) // 占据65%的屏幕高度
                            .background(Color.Black)
                    ) {
                        VideoPlayer(
                            video = uiState.video,
                            isPlaying = uiState.isPlaying,
                            currentPosition = uiState.currentPosition,
                            isBuffering = uiState.isBuffering,
                            onPositionChanged = { position ->
                                viewModel.handleAction(VideoPlayerEvent.OnPositionChanged(position))
                            },
                            onDurationChanged = { duration ->
                                viewModel.handleAction(VideoPlayerEvent.OnDurationChanged(duration))
                            },
                            onBufferingChanged = { isBuffering ->
                                viewModel.handleAction(
                                    VideoPlayerEvent.OnBufferingChanged(
                                        isBuffering
                                    )
                                )
                            },
                            onError = { error ->
                                viewModel.handleAction(VideoPlayerEvent.OnError(error))
                            },
                            onReady = {
                                viewModel.handleAction(VideoPlayerEvent.OnVideoReady)
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // 视频控制层
                        if (uiState.showControls) {
                            VideoControls(
                                isPlaying = uiState.isPlaying,
                                currentPosition = uiState.currentPosition,
                                duration = uiState.duration,
                                volume = uiState.volume,
                                playbackSpeed = uiState.playbackSpeed,
                                isFullscreen = uiState.isFullscreen,
                                notes = uiState.notes,
                                onPlayPause = {
                                    if (uiState.isPlaying) {
                                        viewModel.handleAction(VideoPlayerEvent.Pause)
                                    } else {
                                        viewModel.handleAction(VideoPlayerEvent.Play)
                                    }
                                },
                                onSeek = { position ->
                                    viewModel.handleAction(VideoPlayerEvent.SeekTo(position))
                                },
                                onVolumeChange = { volume ->
                                    viewModel.handleAction(VideoPlayerEvent.SetVolume(volume))
                                },
                                onSpeedChange = { speed ->
                                    viewModel.handleAction(VideoPlayerEvent.SetPlaybackSpeed(speed))
                                },
                                onFullscreenToggle = {
                                    viewModel.handleAction(VideoPlayerEvent.ToggleFullscreen)
                                },
                                onAddNote = { position ->
                                    viewModel.handleAction(VideoPlayerEvent.ShowNoteDialog(position))
                                },
                                onNoteClick = { note ->
                                    viewModel.handleAction(VideoPlayerEvent.SelectNote(note))
                                },
                                onBack = onBackClick
                            )
                        }
                    }

                    // 分隔线
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.1f),
                        thickness = 1.dp
                    )

                    // 笔记面板 - 占据剩余的35%空间，可滚动
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.35f)
                            .background(Color.White) // 改为白色背景
                    ) {
                        NotesPanel(
                            notes = uiState.notes,
                            onNoteClick = { note ->
                                viewModel.handleAction(VideoPlayerEvent.SelectNote(note))
                            },
                            onNoteEdit = { note ->
                                viewModel.handleAction(VideoPlayerEvent.EditNote(note))
                            },
                            onNoteDelete = { noteId ->
                                viewModel.handleAction(VideoPlayerEvent.DeleteNote(noteId))
                            },
                            onAddNote = {
                                viewModel.handleAction(VideoPlayerEvent.ShowNoteDialog(uiState.currentPosition))
                            }
                        )
                    }
                }
            }
        }

        // 添加/编辑笔记对话框
        if (uiState.showNoteDialog) {
            AddNoteDialog(
                note = uiState.selectedNote,
                onSave = { content, color ->  // 修改：添加颜色参数
                    if (uiState.selectedNote?.id?.isEmpty() == true) {
                        // 新增笔记
                        viewModel.handleAction(
                            VideoPlayerEvent.AddNote(
                                position = uiState.selectedNote?.position
                                    ?: uiState.currentPosition,
                                content = content,
                                color = color  // 添加颜色参数
                            )
                        )
                    } else {
                        // 编辑笔记
                        uiState.selectedNote?.let { note ->
                            viewModel.handleAction(
                                VideoPlayerEvent.EditNote(
                                    note.copy(
                                        content = content,
                                        color = color
                                    )
                                )  // 添加颜色参数
                            )
                        }
                    }
                },
                onDismiss = {
                    viewModel.handleAction(VideoPlayerEvent.HideNoteDialog)
                }
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "播放出错",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("返回")
            }

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("重试")
            }
        }
    }
}
