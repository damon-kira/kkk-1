package com.kira.learning.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize

/**
 * 视频数据模型
 */
@Parcelize
data class Video(
    val id: String,
    val title: String,
    val url: String,
    val thumbnailUrl: String? = null,
    val description: String? = null,
    val duration: Long = 0L, // 视频时长，毫秒
    val quality: VideoQuality = VideoQuality.AUTO,
    val subtitleUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

/**
 * 视频质量枚举
 */
enum class VideoQuality(val displayName: String) {
    AUTO("自动"),
    LOW("流畅"),
    MEDIUM("标清"),
    HIGH("高清"),
    ULTRA_HIGH("超清")
}

/**
 * 视频笔记实体（数据库）
 */
@Entity(tableName = "video_notes")
data class VideoNoteEntity(
    @PrimaryKey
    val id: String,
    val videoId: String,
    val timestamp: Long, // 笔记在视频中的位置，毫秒
    val content: String,
    val title: String? = null,
    val color: String = NoteColor.BLUE.name, // 添加颜色字段，存储枚举名称
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val formattedTime: String = formatTime(timestamp)
)

/**
 * 视频笔记UI模型
 */
@Parcelize
data class VideoNote(
    val id: String,
    val videoId: String,
    val position: Long, // 笔记在视频中的位置，毫秒
    val content: String,
    val title: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val color: NoteColor = NoteColor.BLUE
) : Parcelable

/**
 * 笔记颜色枚举
 */
enum class NoteColor(val colorCode: String, val displayName: String) {
    BLUE("#2196F3", "蓝色"),
    GREEN("#4CAF50", "绿色"),
    ORANGE("#FF9800", "橙色"),
    RED("#F44336", "红色"),
    PURPLE("#9C27B0", "紫色"),
    YELLOW("#FFEB3B", "黄色")
}

/**
 * 将颜色代码转换为Compose Color
 */
fun NoteColor.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this.colorCode))
}

/**
 * 格式化时间工具函数 - 公共函数供整个模块使用
 */
fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

/**
 * 实体转换扩展函数
 */
fun VideoNoteEntity.toVideoNote(): VideoNote {
    return VideoNote(
        id = id,
        videoId = videoId,
        position = timestamp,
        content = content,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt,
        color = try {
            NoteColor.valueOf(color)
        } catch (e: Exception) {
            NoteColor.BLUE // 如果颜色解析失败，使用默认蓝色
        }
    )
}

fun VideoNote.toVideoNoteEntity(): VideoNoteEntity {
    return VideoNoteEntity(
        id = id,
        videoId = videoId,
        timestamp = position,
        content = content,
        title = title,
        color = color.name, // 将枚举转换为字符串存储
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
