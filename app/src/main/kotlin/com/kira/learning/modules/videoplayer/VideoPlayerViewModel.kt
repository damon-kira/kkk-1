package com.kira.learning.modules.videoplayer

import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.models.Video
import com.kira.learning.models.VideoNote
import com.kira.learning.modules.videoplayer.repository.VideoPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * 视频播放器ViewModel
 * 基于项目的BaseViewModel架构，负责处理视频播放、笔记管理等业务逻辑
 */
@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val repository: VideoPlayerRepository
) : BaseViewModel<VideoPlayerViewState, VideoPlayerEvent>(VideoPlayerViewState()) {

    override fun handleAction(action: VideoPlayerEvent) {
        when (action) {
            is VideoPlayerEvent.LoadVideo -> loadVideo(action.video)
            is VideoPlayerEvent.Play -> play()
            is VideoPlayerEvent.Pause -> pause()
            is VideoPlayerEvent.Stop -> stop()
            is VideoPlayerEvent.SeekTo -> seekToPosition(action.position)
            is VideoPlayerEvent.SetVolume -> setVolume(action.volume)
            is VideoPlayerEvent.SetPlaybackSpeed -> setPlaybackSpeed(action.speed)
            is VideoPlayerEvent.ToggleFullscreen -> toggleFullscreen()
            is VideoPlayerEvent.ToggleControls -> toggleControls()
            is VideoPlayerEvent.ShowControls -> showControls()
            is VideoPlayerEvent.HideControls -> hideControls()
            is VideoPlayerEvent.AddNote -> addNote(action.position, action.content)
            is VideoPlayerEvent.EditNote -> editNote(action.note)
            is VideoPlayerEvent.DeleteNote -> deleteNote(action.noteId)
            is VideoPlayerEvent.ShowNoteDialog -> showNoteDialog(action.position)
            is VideoPlayerEvent.HideNoteDialog -> hideNoteDialog()
            is VideoPlayerEvent.SelectNote -> selectNote(action.note)
            is VideoPlayerEvent.RetryLoad -> retryLoad()
            is VideoPlayerEvent.OnPositionChanged -> onPositionChanged(action.position)
            is VideoPlayerEvent.OnDurationChanged -> onDurationChanged(action.duration)
            is VideoPlayerEvent.OnBufferingChanged -> onBufferingChanged(action.isBuffering)
            is VideoPlayerEvent.OnError -> onError(action.error)
            is VideoPlayerEvent.OnVideoReady -> onVideoReady()
        }
    }

    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState { copy(isLoading = isLoading) }
    }

    // ===== 视频加载和播放控制 =====

    private fun loadVideo(video: Video) {
        updateState {
            copy(
                video = video,
                isLoading = true,
                error = null,
                currentPosition = 0L,
                duration = 0L
            )
        }

        // 使用BaseViewModel的API调用方法加载视频笔记
        executeApiCall(
            apiCall = { repository.getVideoNotes(video.id) },
            onSuccess = { notes ->
                updateState { copy(notes = notes, isLoading = false) }
            },
            onError = { error ->
                updateState { copy(error = error.message, isLoading = false) }
            }
        )
    }

    private fun retryLoad() {
        currentState.video?.let { video ->
            loadVideo(video)
        }
    }

    private fun play() {
        updateState { copy(isPlaying = true) }
    }

    private fun pause() {
        updateState { copy(isPlaying = false) }
    }

    private fun stop() {
        updateState {
            copy(
                isPlaying = false,
                currentPosition = 0L,
                showControls = true
            )
        }
    }

    private fun seekToPosition(position: Long) {
        updateState { copy(currentPosition = position) }
    }

    private fun setVolume(volume: Float) {
        updateState { copy(volume = volume.coerceIn(0f, 1f)) }
    }

    private fun setPlaybackSpeed(speed: Float) {
        updateState { copy(playbackSpeed = speed) }
    }

    // ===== 界面控制 =====

    private fun toggleFullscreen() {
        updateState { copy(isFullscreen = !isFullscreen) }
    }

    private fun toggleControls() {
        updateState { copy(showControls = !showControls) }
    }

    private fun showControls() {
        updateState { copy(showControls = true) }
    }

    private fun hideControls() {
        updateState { copy(showControls = false) }
    }

    // ===== 笔记管理 =====

    private fun addNote(position: Long, content: String) {
        val video = currentState.video ?: return
        val note = VideoNote(
            id = UUID.randomUUID().toString(),
            videoId = video.id,
            position = position,
            content = content
        )

        executeApiCall(
            apiCall = { repository.saveNote(note) },
            onSuccess = {
                val updatedNotes = currentState.notes + note
                updateState {
                    copy(
                        notes = updatedNotes,
                        showNoteDialog = false,
                        selectedNote = null
                    )
                }
            },
            onError = { error ->
                updateState { copy(error = "保存笔记失败: ${error.message}") }
            }
        )
    }

    private fun editNote(note: VideoNote) {
        executeApiCall(
            apiCall = { repository.updateNote(note) },
            onSuccess = {
                val updatedNotes = currentState.notes.map {
                    if (it.id == note.id) note else it
                }
                updateState {
                    copy(
                        notes = updatedNotes,
                        showNoteDialog = false,
                        selectedNote = null
                    )
                }
            },
            onError = { error ->
                updateState { copy(error = "更新笔记失败: ${error.message}") }
            }
        )
    }

    private fun deleteNote(noteId: String) {
        executeApiCall(
            apiCall = { repository.deleteNote(noteId) },
            onSuccess = {
                val updatedNotes = currentState.notes.filter { it.id != noteId }
                updateState { copy(notes = updatedNotes) }
            },
            onError = { error ->
                updateState { copy(error = "删除笔记失败: ${error.message}") }
            }
        )
    }

    private fun showNoteDialog(position: Long) {
        updateState {
            copy(
                showNoteDialog = true,
                selectedNote = VideoNote(
                    id = "",
                    videoId = currentState.video?.id ?: "",
                    position = position,
                    content = ""
                )
            )
        }
    }

    private fun hideNoteDialog() {
        updateState {
            copy(
                showNoteDialog = false,
                selectedNote = null
            )
        }
    }

    private fun selectNote(note: VideoNote) {
        updateState { copy(selectedNote = note) }
        seekToPosition(note.position)
    }

    // ===== 播放器状态更新 =====

    private fun onPositionChanged(position: Long) {
        updateState { copy(currentPosition = position) }
    }

    private fun onDurationChanged(duration: Long) {
        updateState { copy(duration = duration) }
    }

    private fun onBufferingChanged(isBuffering: Boolean) {
        updateState { copy(isBuffering = isBuffering) }
    }

    private fun onError(error: String) {
        updateState {
            copy(
                error = error,
                isLoading = false,
                isPlaying = false
            )
        }
    }

    private fun onVideoReady() {
        updateState {
            copy(
                isLoading = false,
                error = null
            )
        }
    }
}