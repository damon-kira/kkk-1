package com.kira.learning.modules.videoplayer.repository

import com.kira.learning.models.dao.VideoNoteDao
import com.kira.learning.models.VideoNote
import com.kira.learning.models.toVideoNote
import com.kira.learning.models.toVideoNoteEntity
import com.kira.learning.network.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 视频播放器数据仓库实现
 * 集成现有的Base架构，使用本地数据库存储笔记
 */
@Singleton
class VideoPlayerRepositoryImpl @Inject constructor(
    private val videoNoteDao: VideoNoteDao
) : VideoPlayerRepository {

    override suspend fun getVideoNotes(videoId: String): ApiResult<List<VideoNote>> {
        return try {
            val entities = videoNoteDao.getVideoNotes(videoId)
            val notes = entities.map { it.toVideoNote() }
            ApiResult.Success(notes)
        } catch (e: Exception) {
            ApiResult.Error(
                message = "获取视频笔记失败: ${e.message}", throwable = e
            )
        }
    }

    override suspend fun saveNote(note: VideoNote): ApiResult<Unit> {
        return try {
            val entity = note.toVideoNoteEntity()
            videoNoteDao.insertNote(entity)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(
                message = "保存笔记失败: ${e.message}", throwable = e
            )
        }
    }

    override suspend fun updateNote(note: VideoNote): ApiResult<Unit> {
        return try {
            val entity = note.toVideoNoteEntity()
            videoNoteDao.updateNote(entity)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(
                message = "更新笔记失败: ${e.message}", throwable = e
            )
        }
    }

    override suspend fun deleteNote(noteId: String): ApiResult<Unit> {
        return try {
            videoNoteDao.deleteNote(noteId)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(
                message = "删除笔记失败: ${e.message}", throwable = e
            )
        }
    }

    override fun getVideoNotesFlow(videoId: String): Flow<List<VideoNote>> {
        return videoNoteDao.getVideoNotesFlow(videoId).map { entities ->
            entities.map { it.toVideoNote() }
        }
    }
}
