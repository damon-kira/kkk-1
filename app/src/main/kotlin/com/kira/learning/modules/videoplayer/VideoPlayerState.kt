package com.kira.learning.modules.videoplayer

import com.kira.learning.base.mvi.ViewEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.models.Video
import com.kira.learning.models.VideoNote
import com.kira.learning.models.NoteColor

/**
 * 视频播放器的状态定义
 */
data class VideoPlayerViewState(
    val video: Video? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isLoading: Boolean = false,
    val error: String? = null,
    val notes: List<VideoNote> = emptyList(),
    val isFullscreen: Boolean = false,
    val volume: Float = 1.0f,
    val playbackSpeed: Float = 1.0f,
    val showControls: Boolean = true,
    val isBuffering: Boolean = false,
    val showNoteDialog: Boolean = false,
    val selectedNote: VideoNote? = null
) : ViewState()

/**
 * 视频播放器的事件定义
 */
sealed interface VideoPlayerEvent : ViewEvent {
    // 播放控制事件
    object Play : VideoPlayerEvent
    object Pause : VideoPlayerEvent
    object Stop : VideoPlayerEvent
    data class SeekTo(val position: Long) : VideoPlayerEvent
    data class SetVolume(val volume: Float) : VideoPlayerEvent
    data class SetPlaybackSpeed(val speed: Float) : VideoPlayerEvent

    // 界面控制事件
    object ToggleFullscreen : VideoPlayerEvent
    object ToggleControls : VideoPlayerEvent
    object ShowControls : VideoPlayerEvent
    object HideControls : VideoPlayerEvent

    // 笔记相关事件
    data class AddNote(
        val position: Long,
        val content: String,
        val color: NoteColor = NoteColor.BLUE
    ) : VideoPlayerEvent

    data class EditNote(val note: VideoNote) : VideoPlayerEvent
    data class DeleteNote(val noteId: String) : VideoPlayerEvent
    data class ShowNoteDialog(val position: Long) : VideoPlayerEvent
    object HideNoteDialog : VideoPlayerEvent
    data class SelectNote(val note: VideoNote) : VideoPlayerEvent

    // 视频加载事件
    data class LoadVideo(val video: Video) : VideoPlayerEvent
    object RetryLoad : VideoPlayerEvent

    // 播放器状态更新事件
    data class OnPositionChanged(val position: Long) : VideoPlayerEvent
    data class OnDurationChanged(val duration: Long) : VideoPlayerEvent
    data class OnBufferingChanged(val isBuffering: Boolean) : VideoPlayerEvent
    data class OnError(val error: String) : VideoPlayerEvent
    object OnVideoReady : VideoPlayerEvent
}
