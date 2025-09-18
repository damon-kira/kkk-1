package com.kira.learning.modules.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kira.learning.models.Video
import com.kira.learning.modules.videoplayer.ui.VideoPlayerScreen

/**
 * 视频播放器路由组件
 * 用于导航系统集成
 */
@Composable
fun VideoPlayerRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    // 创建示例视频用于演示
    val demoVideo = Video(
        id = "demo_video",
        title = "Big Buck Bunny - 演示视频",
        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        description = "开源动画短片，支持笔记功能的视频播放器演示",
        duration = 596000 // 约10分钟
    )

    VideoPlayerScreen(
        video = demoVideo,
        onBackClick = onNavigateBack
    )
}
