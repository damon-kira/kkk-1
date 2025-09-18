package com.kira.learning.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kira.learning.models.VideoNoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * 视频笔记数据访问对象
 * 使用VideoNoteEntity作为数据库实体
 */
@Dao
interface VideoNoteDao {

    @Query("SELECT * FROM video_notes WHERE videoId = :videoId ORDER BY timestamp ASC")
    suspend fun getVideoNotes(videoId: String): List<VideoNoteEntity>

    @Query("SELECT * FROM video_notes WHERE videoId = :videoId ORDER BY timestamp ASC")
    fun getVideoNotesFlow(videoId: String): Flow<List<VideoNoteEntity>>

    @Query("SELECT * FROM video_notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): VideoNoteEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertNote(note: VideoNoteEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertNotes(notes: List<VideoNoteEntity>)

    @Update
    suspend fun updateNote(note: VideoNoteEntity)

    @Query("DELETE FROM video_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: String)

    @Query("DELETE FROM video_notes WHERE videoId = :videoId")
    suspend fun deleteVideoNotes(videoId: String)

    @Query("SELECT COUNT(*) FROM video_notes WHERE videoId = :videoId")
    suspend fun getNotesCount(videoId: String): Int
}