package com.kira.learning.modules.videoplayer.repository

import com.kira.learning.models.VideoNote
import com.kira.learning.network.ApiResult
import kotlinx.coroutines.flow.Flow

/**
 * 视频播放器数据仓库接口
 */
interface VideoPlayerRepository {

    /**
     * 获取视频的所有笔记
     */
    suspend fun getVideoNotes(videoId: String): ApiResult<List<VideoNote>>

    /**
     * 保存笔记
     */
    suspend fun saveNote(note: VideoNote): ApiResult<Unit>

    /**
     * 更新笔记
     */
    suspend fun updateNote(note: VideoNote): ApiResult<Unit>

    /**
     * 删除笔记
     */
    suspend fun deleteNote(noteId: String): ApiResult<Unit>

    /**
     * 获取笔记流
     */
    fun getVideoNotesFlow(videoId: String): Flow<List<VideoNote>>
}
